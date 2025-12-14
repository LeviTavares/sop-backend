
package br.ce.sop.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "empenho", uniqueConstraints = @UniqueConstraint(columnNames = "numero_empenho"))
public class Empenho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_empenho", nullable = false, length = 12)
    private String numeroEmpenho;

    @Column(nullable = false)
    private LocalDate dataEmpenho;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "despesa_id")
    private Despesa despesa;

    @OneToMany(mappedBy = "empenho", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Pagamento> pagamentos = new ArrayList<>();
}
