
package br.ce.sop.service;

import br.ce.sop.domain.Despesa;
import br.ce.sop.domain.TipoDespesa;
import br.ce.sop.domain.Pagamento;
import br.ce.sop.domain.Empenho;
import br.ce.sop.domain.StatusDespesa;
import br.ce.sop.dto.DespesaCreateDTO;
import br.ce.sop.exception.BusinessException;
import br.ce.sop.exception.NotFoundException;
import br.ce.sop.mapper.DespesaMapper;
import br.ce.sop.repository.DespesaRepository;
import br.ce.sop.repository.EmpenhoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DespesaService {
  private final DespesaRepository repo;
  private final EmpenhoRepository empRepo;
  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  public DespesaService(DespesaRepository repo, EmpenhoRepository empRepo) {
    this.repo = repo; this.empRepo = empRepo;
  }

  public Page<Despesa> listar(String tipoDespesa, String credor, String numeroProtocolo,
                              String dataInicio, String dataFim, Pageable pageable) {
    Specification<Despesa> spec = (root, query, cb) -> {
      List<jakarta.persistence.criteria.Predicate> preds = new ArrayList<>();
      if (tipoDespesa != null && !tipoDespesa.isBlank()) {
        try {
          TipoDespesa td = TipoDespesa.valueOf(tipoDespesa.toUpperCase().replace(' ', '_'));
          preds.add(cb.equal(root.get("tipoDespesa"), td));
        } catch (IllegalArgumentException ignored) {}
      }
      if (credor != null && !credor.isBlank()) {
        preds.add(cb.like(cb.lower(root.get("credor")), "%" + credor.toLowerCase() + "%"));
      }
      if (numeroProtocolo != null && !numeroProtocolo.isBlank()) {
        preds.add(cb.equal(root.get("numeroProtocolo"), numeroProtocolo));
      }
      if (dataInicio != null && !dataInicio.isBlank()) {
        LocalDateTime di = LocalDateTime.parse(dataInicio + " 00:00", DTF);
        preds.add(cb.greaterThanOrEqualTo(root.get("dataProtocolo"), di));
      }
      if (dataFim != null && !dataFim.isBlank()) {
        LocalDateTime df = LocalDateTime.parse(dataFim + " 23:59", DTF);
        preds.add(cb.lessThanOrEqualTo(root.get("dataProtocolo"), df));
      }
      return cb.and(preds.toArray(new jakarta.persistence.criteria.Predicate[0]));
    };
    return repo.findAll(spec, pageable);
  }

  public Despesa buscar(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Despesa não encontrada"));
  }

  @Transactional
  public Despesa criar(DespesaCreateDTO dto) {
    repo.findByNumeroProtocolo(dto.numeroProtocolo()).ifPresent(d -> { throw new BusinessException("Número de protocolo já existente"); });
    Despesa d = DespesaMapper.toEntity(dto);
    return repo.save(d);
  }

  @Transactional
  public Despesa atualizar(Long id, DespesaCreateDTO dto) {
    Despesa d = buscar(id);
    Despesa novo = DespesaMapper.toEntity(dto);
    d.setNumeroProtocolo(novo.getNumeroProtocolo());
    d.setDescricao(novo.getDescricao());
    d.setCredor(novo.getCredor());
    d.setValor(novo.getValor());
    d.setTipoDespesa(novo.getTipoDespesa());
    d.setDataProtocolo(novo.getDataProtocolo());
    d.setDataVencimento(novo.getDataVencimento());
    return repo.save(d);
  }

  @Transactional
  public void excluir(Long id) {
    Despesa d = buscar(id);
    int countEmp = empRepo.countByDespesa_Id(id);
    if (countEmp > 0) throw new BusinessException("Não é permitido excluir despesa com empenhos");
    repo.delete(d);
  }

  public StatusDespesa calcularStatus(Long id) {
    Despesa despesa = buscar(id);

    BigDecimal valorDespesa = despesa.getValor();

    BigDecimal somaEmpenhos = despesa.getEmpenhos().stream()
            .map(Empenho::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal somaPagamentos = despesa.getEmpenhos().stream()
            .flatMap(e -> e.getPagamentos().stream())
            .map(Pagamento::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (somaEmpenhos.compareTo(BigDecimal.ZERO) == 0) {
      return StatusDespesa.AGUARDANDO_EMPENHO;
    }
    if (somaEmpenhos.compareTo(valorDespesa) < 0) {
      return StatusDespesa.PARCIALMENTE_EMPENHADA;
    }
    if (somaEmpenhos.compareTo(valorDespesa) == 0 && somaPagamentos.compareTo(BigDecimal.ZERO) == 0) {
      return StatusDespesa.AGUARDANDO_PAGAMENTO;
    }
    if (somaPagamentos.compareTo(valorDespesa) < 0 && somaPagamentos.compareTo(BigDecimal.ZERO) > 0) {
      return StatusDespesa.PARCIALMENTE_PAGA;
    }
    if (somaPagamentos.compareTo(valorDespesa) == 0) {
      return StatusDespesa.PAGA;
    }

    return StatusDespesa.AGUARDANDO_EMPENHO; // fallback
  }

}
