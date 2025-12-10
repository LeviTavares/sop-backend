
package br.ce.sop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PagamentoCreateDTO(
  @NotBlank
  @Pattern(
    regexp = "^\d{4}NP\d{4}$",
    message = "Formato do número de pagamento inválido. Ex.: 2025NP0012"
  ) String numeroPagamento,
  @NotBlank String dataPagamento, // dd/MM/yyyy
  @NotNull @DecimalMin("0.00") BigDecimal valor,
  String observacao
) {}
