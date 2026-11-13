
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.Cancelamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CancelamentoRepository
                extends JpaRepository<Cancelamento, Long> {

        Optional<Cancelamento> findByNumeroProtocoloAndDataProtocolo(
                        String numeroProtocolo,
                        LocalDate dataProtocolo);

        boolean existsByNumeroProtocoloAndDataProtocolo(
                        String numeroProtocolo,
                        LocalDate dataProtocolo);

        @Query(value = """
                        SELECT ca.*
                        FROM cancelamentos ca
                        WHERE EXISTS (
                            SELECT 1
                            FROM campanha_titulos ct
                            JOIN titulos t ON t.id = ct.titulo_id
                            WHERE ct.campanha_id = :campanhaId
                              AND ca.data_protocolo = t.data_protocolo
                              AND COALESCE(
                                  NULLIF(LTRIM(ca.numero_protocolo, '0'), ''),
                                  '0'
                              ) = COALESCE(
                                  NULLIF(LTRIM(t.numero_protocolo, '0'), ''),
                                  '0'
                              )
                        )
                        """, nativeQuery = true)
        List<Cancelamento> buscarCancelamentosPorCampanha(
                        @Param("campanhaId") Long campanhaId);
}
