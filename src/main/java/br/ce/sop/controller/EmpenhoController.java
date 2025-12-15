
package br.ce.sop.controller;

import br.ce.sop.dto.EmpenhoCreateDTO;
import br.ce.sop.dto.EmpenhoDTO;
import br.ce.sop.dto.PagamentoCreateDTO;
import br.ce.sop.dto.PagamentoDTO;
import br.ce.sop.mapper.EmpenhoMapper;
import br.ce.sop.mapper.PagamentoMapper;
import br.ce.sop.service.EmpenhoService;
import br.ce.sop.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empenhos")
public class EmpenhoController {
  private final EmpenhoService empenhoService;
  private final PagamentoService pagamentoService;
  public EmpenhoController(EmpenhoService empenhoService, PagamentoService pagamentoService) {
    this.empenhoService = empenhoService;
    this.pagamentoService = pagamentoService;
  }

  @GetMapping
  public Page<EmpenhoDTO> listar(
      @RequestParam(name = "despesaId", required = false) Long despesaId,
      @RequestParam(name = "numeroEmpenho", required = false) String numeroEmpenho,
      @RequestParam(name = "dataInicio", required = false) String dataInicio, // dd/MM/yyyy
      @RequestParam(name = "dataFim", required = false) String dataFim,    // dd/MM/yyyy
      Pageable pageable) {
    return empenhoService.listar(despesaId, numeroEmpenho, dataInicio, dataFim, pageable)
                  .map(EmpenhoMapper::toDTO);
  }

  @GetMapping("/{id}")
  public EmpenhoDTO buscar(@PathVariable("id") Long id) { return EmpenhoMapper.toDTO(empenhoService.buscar(id)); }

  @GetMapping("/{id}/pagamentos")
  public Page<PagamentoDTO> listarPagamentos(@PathVariable("id") Long empenhoId, Pageable pageable) {
    return pagamentoService.listarPorEmpenho(empenhoId, pageable)
            .map(PagamentoMapper::toDTO);
  }

  @PostMapping("/{id}/pagamentos")
  public PagamentoDTO criar(@PathVariable("id") Long empenhoId,
                            @Valid @RequestBody PagamentoCreateDTO dto) {
    return PagamentoMapper.toDTO(pagamentoService.criar(empenhoId, dto));
  }

  @PutMapping("/{id}")
  public EmpenhoDTO atualizar(@PathVariable("id") Long id,
                              @Valid @RequestBody EmpenhoCreateDTO dto) {
    return EmpenhoMapper.toDTO(empenhoService.atualizar(id, dto));
  }

  @DeleteMapping("/{id}")
  public void excluir(@PathVariable("id") Long id) { empenhoService.excluir(id); }
}
