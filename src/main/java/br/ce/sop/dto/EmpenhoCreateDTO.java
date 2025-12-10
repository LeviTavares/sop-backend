
package br.ce.sop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record EmpenhoCreateDTO(
  @NotBlank
  @Pattern(
    regexp = "^\d{4}NE\d{4}$",
    message = "Formato do número de empenho inválido. Ex.: 2025NE0003"
  ) String numeroEmpenho,
  @NotBlank String dataEmpenho, // dd/MM/yyyy
  @NotNull @DecimalMin("0.00") BigDecimal valor,
  String observacao
) {}
