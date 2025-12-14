
package br.ce.sop.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "pagamento", uniqueConstraints = @UniqueConstraint(columnNames = "numero_pagamento"))
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_pagamento", nullable = false, length = 12)
    private String numeroPagamento;

    @Column(nullable = false)
    private LocalDate dataPagamento;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empenho_id")
    private Empenho empenho;
}
