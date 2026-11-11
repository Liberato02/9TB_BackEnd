
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.Apresentante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApresentanteRepository
        extends JpaRepository<Apresentante, Long> {

    Optional<Apresentante> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
