package br.ce.sop.mapper;

import br.ce.sop.domain.Pagamento;
import br.ce.sop.dto.PagamentoCreateDTO;
import br.ce.sop.dto.PagamentoDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PagamentoMapper {
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Pagamento toEntity(PagamentoCreateDTO dto) {
        Pagamento p = new Pagamento();
        p.setNumeroPagamento(dto.numeroPagamento());
        p.setDataPagamento(LocalDate.parse(dto.dataPagamento(), DF));
        p.setValor(dto.valor());
        p.setObservacao(dto.observacao());
        return p;
    }

    public static PagamentoDTO toDTO(Pagamento p) {
        return new PagamentoDTO(
                p.getId(),
                p.getNumeroPagamento(),
                p.getDataPagamento().format(DF),
                p.getValor(),
                p.getObservacao(),
                p.getEmpenho().getId()
        );
    }
}