
package br.ce.sop.controller;

import br.ce.sop.dto.PagamentoCreateDTO;
import br.ce.sop.dto.PagamentoDTO;
import br.ce.sop.mapper.PagamentoMapper;
import br.ce.sop.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PagamentoController {
  private final PagamentoService service;
  public PagamentoController(PagamentoService service) { this.service = service; }

  @GetMapping("/api/pagamentos")
  public Page<PagamentoDTO> listar(
      @RequestParam(required = false) Long empenhoId,
      @RequestParam(required = false) String numeroPagamento,
      @RequestParam(required = false) String dataInicio, // dd/MM/yyyy
      @RequestParam(required = false) String dataFim,    // dd/MM/yyyy
      Pageable pageable) {
    return service.listar(empenhoId, numeroPagamento, dataInicio, dataFim, pageable)
                  .map(PagamentoMapper::toDTO);
  }

  @GetMapping("/api/pagamentos/{id}")
  public PagamentoDTO buscar(@PathVariable Long id) { return PagamentoMapper.toDTO(service.buscar(id)); }

  @PostMapping("/api/empenhos/{empenhoId}/pagamentos")
  public PagamentoDTO criar(@PathVariable Long empenhoId, @Valid @RequestBody PagamentoCreateDTO dto) {
    return PagamentoMapper.toDTO(service.criar(empenhoId, dto));
  }

  @PutMapping("/api/pagamentos/{id}")
  public PagamentoDTO atualizar(@PathVariable Long id, @Valid @RequestBody PagamentoCreateDTO dto) {
    return PagamentoMapper.toDTO(service.atualizar(id, dto));
  }

  @DeleteMapping("/api/pagamentos/{id}")
  public void excluir(@PathVariable Long id) { service.excluir(id); }
}
