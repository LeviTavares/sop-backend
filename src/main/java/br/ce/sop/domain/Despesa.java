
package br.ce.sop.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "despesa", uniqueConstraints = @UniqueConstraint(columnNames = "numero_protocolo"))
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_protocolo", nullable = false, length = 20)
    private String numeroProtocolo;

    @Enumerated(EnumType.STRING)
    private TipoDespesa tipoDespesa;

    @Column(nullable = false)
    private LocalDateTime dataProtocolo;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    @Column(nullable = false)
    private String credor;

    @Column(nullable = false, length = 1024)
    private String descricao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @OneToMany(mappedBy = "despesa", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Empenho> empenhos = new ArrayList<>();
}
