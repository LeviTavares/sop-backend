
package br.ce.sop.controller;

import br.ce.sop.dto.EmpenhoCreateDTO;
import br.ce.sop.dto.EmpenhoDTO;
import br.ce.sop.mapper.EmpenhoMapper;
import br.ce.sop.service.EmpenhoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class EmpenhoController {
  private final EmpenhoService service;
  public EmpenhoController(EmpenhoService service) { this.service = service; }

  @GetMapping("/api/empenhos")
  public Page<EmpenhoDTO> listar(
      @RequestParam(required = false) Long despesaId,
      @RequestParam(required = false) String numeroEmpenho,
      @RequestParam(required = false) String dataInicio, // dd/MM/yyyy
      @RequestParam(required = false) String dataFim,    // dd/MM/yyyy
      Pageable pageable) {
    return service.listar(despesaId, numeroEmpenho, dataInicio, dataFim, pageable)
                  .map(EmpenhoMapper::toDTO);
  }

  @GetMapping("/api/empenhos/{id}")
  public EmpenhoDTO buscar(@PathVariable Long id) { return EmpenhoMapper.toDTO(service.buscar(id)); }

  @PostMapping("/api/despesas/{despesaId}/empenhos")
  public EmpenhoDTO criar(@PathVariable Long despesaId, @Valid @RequestBody EmpenhoCreateDTO dto) {
    return EmpenhoMapper.toDTO(service.criar(despesaId, dto));
  }

  @PutMapping("/api/empenhos/{id}")
  public EmpenhoDTO atualizar(@PathVariable Long id, @Valid @RequestBody EmpenhoCreateDTO dto) {
    return EmpenhoMapper.toDTO(service.atualizar(id, dto));
  }

  @DeleteMapping("/api/empenhos/{id}")
  public void excluir(@PathVariable Long id) { service.excluir(id); }
}
