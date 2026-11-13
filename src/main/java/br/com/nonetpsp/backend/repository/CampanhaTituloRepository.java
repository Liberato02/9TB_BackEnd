
package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.CampanhaTitulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampanhaTituloRepository
                extends JpaRepository<CampanhaTitulo, Long> {

        boolean existsByCampanhaIdAndTituloId(
                        Long campanhaId,
                        Long tituloId);

        @Query("""
                        SELECT COUNT(ct)
                        FROM CampanhaTitulo ct
                        WHERE ct.campanha.id = :campanhaId
                        """)
        long contarTitulosPorCampanha(
                        @Param("campanhaId") Long campanhaId);

        @Query(value = """
                        SELECT COUNT(DISTINCT ct.titulo_id)
                        FROM campanha_titulos ct
                        JOIN titulos t
                            ON t.id = ct.titulo_id
                        WHERE ct.campanha_id = :campanhaId
                          AND EXISTS (
                              SELECT 1
                              FROM cancelamentos ca
                              WHERE ca.data_protocolo = t.data_protocolo
                                AND COALESCE(
                                    NULLIF(LTRIM(ca.numero_protocolo, '0'), ''),
                                    '0'
                                ) = COALESCE(
                                    NULLIF(LTRIM(t.numero_protocolo, '0'), ''),
                                    '0'
                                )
                          )
                        """, nativeQuery = true)
        long contarTitulosCanceladosPorCampanha(
                        @Param("campanhaId") Long campanhaId);

        @Query("""
                        SELECT ct
                        FROM CampanhaTitulo ct
                        JOIN FETCH ct.titulo t
                        JOIN FETCH t.apresentante
                        WHERE ct.campanha.id = :campanhaId
                        ORDER BY t.dataProtocolo DESC, t.numeroProtocolo
                        """)
        List<CampanhaTitulo> listarTitulosPorCampanha(
                        @Param("campanhaId") Long campanhaId);
}
