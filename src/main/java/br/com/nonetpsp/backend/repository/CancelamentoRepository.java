
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.Cancelamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CancelamentoRepository
        extends JpaRepository<Cancelamento, Long> {

    Optional<Cancelamento> findByNumeroProtocoloAndDataProtocolo(
            String numeroProtocolo,
            LocalDate dataProtocolo);

    boolean existsByNumeroProtocoloAndDataProtocolo(
            String numeroProtocolo,
            LocalDate dataProtocolo);
}
