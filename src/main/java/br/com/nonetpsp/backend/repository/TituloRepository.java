
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TituloRepository
                extends JpaRepository<Titulo, Long> {

        boolean existsByNumeroProtocoloAndDataProtocolo(
                        String numeroProtocolo,
                        LocalDate dataProtocolo);

        Optional<Titulo> findByNumeroProtocoloAndDataProtocolo(
                        String numeroProtocolo,
                        LocalDate dataProtocolo);
}
