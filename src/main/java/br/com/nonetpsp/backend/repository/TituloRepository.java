
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.Titulo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

        @Query(value = """
                        SELECT t.*
                        FROM titulos t
                        WHERE t.data_protocolo = :dataProtocolo
                          AND COALESCE(
                              NULLIF(LTRIM(t.numero_protocolo, '0'), ''),
                              '0'
                          ) = COALESCE(
                              NULLIF(LTRIM(CAST(:numeroProtocolo AS TEXT), '0'), ''),
                              '0'
                          )
                        """, nativeQuery = true)
        Optional<Titulo> buscarPorProtocoloNormalizado(
                        @Param("numeroProtocolo") String numeroProtocolo,
                        @Param("dataProtocolo") LocalDate dataProtocolo);
}
