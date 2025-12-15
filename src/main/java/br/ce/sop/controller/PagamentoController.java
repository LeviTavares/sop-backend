
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
@RequestMapping("/api/pagamentos")
public class PagamentoController {
  private final PagamentoService service;
  public PagamentoController(PagamentoService service) { this.service = service; }

  @GetMapping
  public Page<PagamentoDTO> listar(
      @RequestParam(name = "empenhoId", required = false) Long empenhoId,
      @RequestParam(name = "numeroPagamento", required = false) String numeroPagamento,
      @RequestParam(name = "dataInicio", required = false) String dataInicio, // dd/MM/yyyy
      @RequestParam(name = "dataFim", required = false) String dataFim,    // dd/MM/yyyy
      Pageable pageable) {
    return service.listar(empenhoId, numeroPagamento, dataInicio, dataFim, pageable)
                  .map(PagamentoMapper::toDTO);
  }

  @GetMapping("/{id}")
  public PagamentoDTO buscar(@PathVariable("id") Long id) { return PagamentoMapper.toDTO(service.buscar(id)); }



  @PutMapping("/{id}")
  public PagamentoDTO atualizar(@PathVariable("id") Long id,
                                @Valid @RequestBody PagamentoCreateDTO dto) {
    return PagamentoMapper.toDTO(service.atualizar(id, dto));
  }

  @DeleteMapping("/{id}")
  public void excluir(@PathVariable("id") Long id) { service.excluir(id); }
}
