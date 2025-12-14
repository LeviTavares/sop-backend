package br.ce.sop.dto;

import java.math.BigDecimal;

public record PagamentoDTO(
        Long id,
        String numeroPagamento,
        String dataPagamento,
        BigDecimal valor,
        String observacao,
        Long empenhoId
) {
}
