package br.ce.sop.dto;

import java.math.BigDecimal;

public record EmpenhoDTO(
        Long id,
        String numeroEmpenho,
        String dataEmpenho,
        BigDecimal valor,
        String observacao,
        Long despesaId
) {
}
