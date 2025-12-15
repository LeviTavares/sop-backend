
package br.ce.sop.repository;

import br.ce.sop.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long>, JpaSpecificationExecutor<Pagamento> {
  @Query("select coalesce(sum(p.valor),0) from Pagamento p where p.empenho.id = :empenhoId")
  BigDecimal totalByEmpenho(@Param("empenhoId") Long empenhoId);

  boolean existsByNumeroPagamento(String numeroPagamento);
  int countByEmpenho_Id(Long empenhoId);

  Page<Pagamento> findByEmpenho_Id(Long empenhoId, Pageable pageable);
}
