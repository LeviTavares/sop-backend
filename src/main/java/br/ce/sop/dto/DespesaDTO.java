package br.ce.sop.dto;

import java.math.BigDecimal;

public record DespesaDTO(
        Long id,
        String numeroProtocolo,
        String tipoDespesa,
        String dataProtocolo,
        String dataVencimento,
        String credor,
        String descricao,
        BigDecimal valor
) {
}
