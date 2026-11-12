
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.CampanhaTitulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampanhaTituloRepository
        extends JpaRepository<CampanhaTitulo, Long> {

    boolean existsByCampanhaIdAndTituloId(
            Long campanhaId,
            Long tituloId);
}
