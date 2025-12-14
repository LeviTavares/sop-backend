package br.ce.sop.mapper;

import br.ce.sop.domain.Despesa;
import br.ce.sop.domain.TipoDespesa;
import br.ce.sop.dto.DespesaCreateDTO;
import br.ce.sop.dto.DespesaDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DespesaMapper {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Despesa toEntity(DespesaCreateDTO dto){
        Despesa d = new Despesa();
        d.setNumeroProtocolo(dto.numeroProtocolo());
        if (dto.tipoDespesa() != null && !dto.tipoDespesa().isBlank()){
            d.setTipoDespesa(TipoDespesa.valueOf(dto.tipoDespesa().toUpperCase().replace(' ', '_')));
        }
        d.setDataProtocolo(LocalDateTime.parse(dto.dataProtocolo(), DTF));
        d.setDataVencimento(LocalDate.parse(dto.dataVencimento(), DF));
        d.setCredor(dto.credor());
        d.setDescricao(dto.descricao());
        d.setValor(dto.valor());
        return d;
    }

    public static DespesaDTO toDTO(Despesa d) {
        String tipo = d.getTipoDespesa() != null ? d.getTipoDespesa().name() : null;
        return new DespesaDTO(
                d.getId(),
                d.getNumeroProtocolo(),
                tipo,
                d.getDataProtocolo().format(DTF),
                d.getDataVencimento().format(DF),
                d.getCredor(),
                d.getDescricao(),
                d.getValor()
        );
    }
}
