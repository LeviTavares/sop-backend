
package br.ce.sop.service;

import br.ce.sop.domain.Despesa;
import br.ce.sop.domain.Empenho;
import br.ce.sop.dto.EmpenhoCreateDTO;
import br.ce.sop.exception.BusinessException;
import br.ce.sop.exception.NotFoundException;
import br.ce.sop.mapper.EmpenhoMapper;
import br.ce.sop.repository.DespesaRepository;
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
public class EmpenhoService {
  private final EmpenhoRepository repo;
  private final DespesaRepository despRepo;
  private final PagamentoRepository pagRepo;
  private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public EmpenhoService(EmpenhoRepository repo, DespesaRepository despRepo, PagamentoRepository pagRepo) {
    this.repo = repo; this.despRepo = despRepo; this.pagRepo = pagRepo;
  }

  public Page<Empenho> listar(Long despesaId, String numeroEmpenho, String dataInicio, String dataFim, Pageable pageable) {
    Specification<Empenho> spec = (root, query, cb) -> {
      List<jakarta.persistence.criteria.Predicate> preds = new ArrayList<>();
      if (despesaId != null) {
        preds.add(cb.equal(root.get("despesa").get("id"), despesaId));
      }
      if (numeroEmpenho != null && !numeroEmpenho.isBlank()) {
        preds.add(cb.equal(root.get("numeroEmpenho"), numeroEmpenho));
      }
      if (dataInicio != null && !dataInicio.isBlank()) {
        LocalDate di = LocalDate.parse(dataInicio, DF);
        preds.add(cb.greaterThanOrEqualTo(root.get("dataEmpenho"), di));
      }
      if (dataFim != null && !dataFim.isBlank()) {
        LocalDate df = LocalDate.parse(dataFim, DF);
        preds.add(cb.lessThanOrEqualTo(root.get("dataEmpenho"), df));
      }
      return cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
    };
    return repo.findAll(spec, pageable);
  }

  public Empenho buscar(Long id) { return repo.findById(id).orElseThrow(() -> new NotFoundException("Empenho não encontrado")); }

  @Transactional
  public Empenho criar(Long despesaId, EmpenhoCreateDTO dto) {
    Despesa d = despRepo.findById(despesaId).orElseThrow(() -> new NotFoundException("Despesa não encontrada"));
    if (repo.existsByNumeroEmpenho(dto.numeroEmpenho())) {
      throw new BusinessException("Número de empenho já existente");
    }
    Empenho e = EmpenhoMapper.toEntity(dto);
    BigDecimal soma = repo.totalByDespesa(despesaId);
    if (soma.add(e.getValor()).compareTo(d.getValor()) > 0) {
      throw new BusinessException("Soma dos empenhos ultrapassa o valor da despesa");
    }
    e.setDespesa(d);
    return repo.save(e);
  }

  @Transactional
  public Empenho atualizar(Long id, EmpenhoCreateDTO dto) {
    Empenho e = buscar(id);
    Empenho novo = EmpenhoMapper.toEntity(dto);
    BigDecimal somaOutros = repo.totalByDespesa(e.getDespesa().getId()).subtract(e.getValor());
    if (somaOutros.add(novo.getValor()).compareTo(e.getDespesa().getValor()) > 0) {
      throw new BusinessException("Soma dos empenhos ultrapassa o valor da despesa");
    }
    e.setNumeroEmpenho(novo.getNumeroEmpenho());
    e.setDataEmpenho(novo.getDataEmpenho());
    e.setValor(novo.getValor());
    e.setObservacao(novo.getObservacao());
    return repo.save(e);
  }

  @Transactional
  public void excluir(Long id) {
    Empenho e = buscar(id);
    int countPag = pagRepo.countByEmpenho_Id(id);
    if (countPag > 0) throw new BusinessException("Não é permitido excluir empenho com pagamentos");
    repo.delete(e);
  }
}
