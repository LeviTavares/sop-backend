
package br.ce.sop.repository;

import br.ce.sop.domain.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {
  Optional<Despesa> findByNumeroProtocolo(String numeroProtocolo);
}
