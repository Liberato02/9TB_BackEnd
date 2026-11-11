
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.ApresentanteAlias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApresentanteAliasRepository
        extends JpaRepository<ApresentanteAlias, Long> {

    Optional<ApresentanteAlias> findByNomeAlternativoIgnoreCase(
            String nomeAlternativo);

    boolean existsByNomeAlternativoIgnoreCase(
            String nomeAlternativo);
}
