
package br.ce.sop.controller;

import br.ce.sop.domain.StatusDespesa;
import br.ce.sop.dto.DespesaCreateDTO;
import br.ce.sop.dto.DespesaDTO;
import br.ce.sop.dto.EmpenhoCreateDTO;
import br.ce.sop.dto.EmpenhoDTO;
import br.ce.sop.mapper.DespesaMapper;
import br.ce.sop.mapper.EmpenhoMapper;
import br.ce.sop.service.DespesaService;
import br.ce.sop.service.EmpenhoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {
  private final DespesaService despesaService;
  private final EmpenhoService empenhoService;
  public DespesaController(DespesaService despesaService, EmpenhoService empenhoService) {
    this.despesaService = despesaService;
    this.empenhoService = empenhoService;
  }

  @GetMapping
  public Page<DespesaDTO> listar(
      @RequestParam(name = "tipoDespesa", required = false) String tipoDespesa,
      @RequestParam(name = "credor", required = false) String credor,
      @RequestParam(name = "numeroProtocolo", required = false) String numeroProtocolo,
      @RequestParam(name = "dataInicio", required = false) String dataInicio, // dd/MM/yyyy
      @RequestParam(name = "dataFim", required = false) String dataFim,    // dd/MM/yyyy
      Pageable pageable
  ) {
    return despesaService.listar(tipoDespesa, credor, numeroProtocolo, dataInicio, dataFim, pageable)
                  .map(DespesaMapper::toDTO);
  }

  @GetMapping("/{id}")
  public DespesaDTO buscar(@PathVariable("id") Long id) { return DespesaMapper.toDTO(despesaService.buscar(id)); }

  @GetMapping("/{id}/status")
  public Map<String, Object> status(@PathVariable("id") Long id){
    StatusDespesa status = despesaService.calcularStatus(id);
    return Map.of(
            "id", id,
            "status", status.name()
    );
  }

  @GetMapping("/{id}/empenhos")
  public Page<EmpenhoDTO> listarEmpenhos(@PathVariable("id") Long despesaId, Pageable pageable) {
    return empenhoService.listarPorDespesa(despesaId, pageable)
            .map(EmpenhoMapper::toDTO);
  }

  @PostMapping
  public DespesaDTO criar(@Valid @RequestBody DespesaCreateDTO dto) {
    return DespesaMapper.toDTO(despesaService.criar(dto));
  }

  @PostMapping("/{id}/empenhos")
  public EmpenhoDTO criarEmpenho(@PathVariable("id") Long despesaId,
                                 @RequestBody EmpenhoCreateDTO dto) {
    return EmpenhoMapper.toDTO(empenhoService.criar(despesaId, dto));
  }

  @PutMapping("/{id}")
  public DespesaDTO atualizar(@PathVariable("id") Long id,
                              @Valid @RequestBody DespesaCreateDTO dto) {
    return DespesaMapper.toDTO(despesaService.atualizar(id, dto));
  }

  @DeleteMapping("/{id}")
  public void excluir(@PathVariable("id") Long id) { despesaService.excluir(id); }
}
