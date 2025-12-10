
package br.ce.sop.controller;

import br.ce.sop.dto.DespesaCreateDTO;
import br.ce.sop.dto.DespesaDTO;
import br.ce.sop.mapper.DespesaMapper;
import br.ce.sop.service.DespesaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {
  private final DespesaService service;
  public DespesaController(DespesaService service) { this.service = service; }

  @GetMapping
  public Page<DespesaDTO> listar(
      @RequestParam(required = false) String tipoDespesa,
      @RequestParam(required = false) String credor,
      @RequestParam(required = false) String numeroProtocolo,
      @RequestParam(required = false) String dataInicio, // dd/MM/yyyy
      @RequestParam(required = false) String dataFim,    // dd/MM/yyyy
      Pageable pageable) {
    return service.listar(tipoDespesa, credor, numeroProtocolo, dataInicio, dataFim, pageable)
                  .map(DespesaMapper::toDTO);
  }

  @GetMapping("/{id}")
  public DespesaDTO buscar(@PathVariable Long id) { return DespesaMapper.toDTO(service.buscar(id)); }

  @PostMapping
  public DespesaDTO criar(@Valid @RequestBody DespesaCreateDTO dto) {
    return DespesaMapper.toDTO(service.criar(dto));
  }

  @PutMapping("/{id}")
  public DespesaDTO atualizar(@PathVariable Long id, @Valid @RequestBody DespesaCreateDTO dto) {
    return DespesaMapper.toDTO(service.atualizar(id, dto));
  }

  @DeleteMapping("/{id}")
  public void excluir(@PathVariable Long id) { service.excluir(id); }
}
