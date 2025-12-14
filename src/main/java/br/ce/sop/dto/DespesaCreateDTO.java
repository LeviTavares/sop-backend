
package br.ce.sop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DespesaCreateDTO(
  @NotBlank
  @Pattern(
    regexp = "^\\d{5}\\.\\d{6}/\\d{4}-\\d{2}$",
    message = "Formato do número de protocolo inválido. Ex.: 43022.123456/2025-01"
  )
  String numeroProtocolo,
  String tipoDespesa, // opcional enum name
  @NotBlank
  String dataProtocolo, // dd/MM/yyyy HH:mm
  @NotBlank
  String dataVencimento, // dd/MM/yyyy
  @NotBlank
  String credor,
  @NotBlank
  String descricao,
  @NotNull
  @DecimalMin("0.00")
  BigDecimal valor
) {}
