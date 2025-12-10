
package br.ce.sop.repository;

import br.ce.sop.domain.Empenho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

public interface EmpenhoRepository extends JpaRepository<Empenho, Long>, JpaSpecificationExecutor<Empenho> {
  @Query("select coalesce(sum(e.valor),0) from Empenho e where e.despesa.id = :despesaId")
  BigDecimal totalByDespesa(@Param("despesaId") Long despesaId);

  boolean existsByNumeroEmpenho(String numeroEmpenho);
  int countByDespesa_Id(Long despesaId);
}
