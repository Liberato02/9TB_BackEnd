
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.repository.CampanhaRepository;
import br.com.nonetpsp.backend.repository.CampanhaTituloRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class IndicadoresCampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final CampanhaTituloRepository campanhaTituloRepository;

    public IndicadoresCampanhaService(
            CampanhaRepository campanhaRepository,
            CampanhaTituloRepository campanhaTituloRepository) {

        this.campanhaRepository = campanhaRepository;
        this.campanhaTituloRepository = campanhaTituloRepository;
    }

    @Transactional(readOnly = true)
    public IndicadoresCampanha consultar(Long campanhaId) {

        if (!campanhaRepository.existsById(campanhaId)) {
            throw new IllegalArgumentException(
                    "Campanha não encontrada: " + campanhaId);
        }

        long totalTitulos = campanhaTituloRepository.contarTitulosPorCampanha(campanhaId);

        long titulosCancelados = campanhaTituloRepository.contarTitulosCanceladosPorCampanha(
                campanhaId);

        long naoEncontrados = totalTitulos - titulosCancelados;

        BigDecimal percentual = totalTitulos == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(titulosCancelados)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                BigDecimal.valueOf(totalTitulos),
                                2,
                                RoundingMode.HALF_UP);

        return new IndicadoresCampanha(
                campanhaId,
                totalTitulos,
                titulosCancelados,
                naoEncontrados,
                percentual);
    }

    public record IndicadoresCampanha(
            Long campanhaId,
            long totalTitulos,
            long titulosCancelados,
            long naoEncontradosNosPdfs,
            BigDecimal percentualCancelados) {
    }
}
