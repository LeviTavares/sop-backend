package br.ce.sop.mapper;

import br.ce.sop.domain.Empenho;
import br.ce.sop.dto.EmpenhoCreateDTO;
import br.ce.sop.dto.EmpenhoDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmpenhoMapper {
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Empenho toEntity(EmpenhoCreateDTO dto) {
        Empenho e = new Empenho();
        e.setNumeroEmpenho(dto.numeroEmpenho());
        e.setDataEmpenho(LocalDate.parse(dto.dataEmpenho(), DF));
        e.setValor(dto.valor());
        e.setObservacao(dto.observacao());
        return e;
    }

    public static EmpenhoDTO toDTO(Empenho e) {
        return new EmpenhoDTO(
                e.getId(),
                e.getNumeroEmpenho(),
                e.getDataEmpenho().format(DF),
                e.getValor(),
                e.getObservacao(),
                e.getDespesa().getId()
        );
    }
}