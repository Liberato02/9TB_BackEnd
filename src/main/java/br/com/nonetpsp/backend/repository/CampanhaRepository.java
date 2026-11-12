
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampanhaRepository
        extends JpaRepository<Campanha, Long> {
}
