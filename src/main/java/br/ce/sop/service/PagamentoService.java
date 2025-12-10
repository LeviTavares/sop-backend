
package br.ce.sop.service;

import br.ce.sop.domain.Empenho;
import br.ce.sop.domain.Pagamento;
import br.ce.sop.dto.PagamentoCreateDTO;
import br.ce.sop.exception.BusinessException;
import br.ce.sop.exception.NotFoundException;
import br.ce.sop.mapper.PagamentoMapper;
import br.ce.sop.repository.EmpenhoRepository;
import br.ce.sop.repository.PagamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagamentoService {
  private final PagamentoRepository repo;
  private final EmpenhoRepository empRepo;
  private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public PagamentoService(PagamentoRepository repo, EmpenhoRepository empRepo) { this.repo = repo; this.empRepo = empRepo; }

  public Page<Pagamento> listar(Long empenhoId, String numeroPagamento, String dataInicio, String dataFim, Pageable pageable) {
    Specification<Pagamento> spec = (root, query, cb) -> {
      List<jakarta.persistence.criteria.Predicate> preds = new ArrayList<>();
      if (empenhoId != null) {
        preds.add(cb.equal(root.get("empenho").get("id"), empenhoId));
      }
      if (numeroPagamento != null && !numeroPagamento.isBlank()) {
        preds.add(cb.equal(root.get("numeroPagamento"), numeroPagamento));
      }
      if (dataInicio != null && !dataInicio.isBlank()) {
        LocalDate di = LocalDate.parse(dataInicio, DF);
        preds.add(cb.greaterThanOrEqualTo(root.get("dataPagamento"), di));
      }
      if (dataFim != null && !dataFim.isBlank()) {
        LocalDate df = LocalDate.parse(dataFim, DF);
        preds.add(cb.lessThanOrEqualTo(root.get("dataPagamento"), df));
      }
      return cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
    };
    return repo.findAll(spec, pageable);
  }

  public Pagamento buscar(Long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Pagamento não encontrado")); }

  @Transactional
  public Pagamento criar(Long empenhoId, PagamentoCreateDTO dto) {
    Empenho e = empRepo.findById(empenhoId).orElseThrow(() -> new NotFoundException("Empenho não encontrado"));
    if (repo.existsByNumeroPagamento(dto.numeroPagamento())) {
      throw new BusinessException("Número de pagamento já existente");
    }
    Pagamento p = PagamentoMapper.toEntity(dto);
    BigDecimal soma = repo.totalByEmpenho(empenhoId);
    if (soma.add(p.getValor()).compareTo(e.getValor()) > 0) {
      throw new BusinessException("Soma dos pagamentos ultrapassa o valor do empenho");
    }
    p.setEmpenho(e);
    return repo.save(p);
  }

  @Transactional
  public Pagamento atualizar(Long id, PagamentoCreateDTO dto) {
    Pagamento p = buscar(id);
    Pagamento novo = PagamentoMapper.toEntity(dto);
    BigDecimal somaOutros = repo.totalByEmpenho(p.getEmpenho().getId()).subtract(p.getValor());
    if (somaOutros.add(novo.getValor()).compareTo(p.getEmpenho().getValor()) > 0) {
      throw new BusinessException("Soma dos pagamentos ultrapassa o valor do empenho");
    }
    p.setNumeroPagamento(novo.getNumeroPagamento());
    p.setDataPagamento(novo.getDataPagamento());
    p.setValor(novo.getValor());
    p.setObservacao(novo.getObservacao());
    return repo.save(p);
  }

  @Transactional
  public void excluir(Long id) { repo.delete(buscar(id)); }
}
