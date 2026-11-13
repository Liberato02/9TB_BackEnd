
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.repository.CampanhaRepository;
import br.com.nonetpsp.backend.repository.CampanhaTituloRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndicadoresCampanhaServiceTest {

    private CampanhaRepository campanhaRepository;
    private CampanhaTituloRepository campanhaTituloRepository;
    private IndicadoresCampanhaService service;

    @BeforeEach
    void configurar() {
        campanhaRepository = mock(CampanhaRepository.class);
        campanhaTituloRepository = mock(CampanhaTituloRepository.class);

        service = new IndicadoresCampanhaService(
                campanhaRepository,
                campanhaTituloRepository);
    }

    @Test
    void deveIdentificarCampanhaComCancelamento() {

        when(campanhaRepository.existsById(3L))
                .thenReturn(true);

        when(campanhaTituloRepository.contarTitulosPorCampanha(3L))
                .thenReturn(1L);

        when(campanhaTituloRepository.contarTitulosCanceladosPorCampanha(3L))
                .thenReturn(1L);

        var resultado = service.consultar(3L);

        assertEquals(1, resultado.totalTitulos());
        assertEquals(1, resultado.titulosCancelados());
        assertEquals(0, resultado.naoEncontradosNosPdfs());

        assertEquals(
                0,
                new BigDecimal("100.00")
                        .compareTo(resultado.percentualCancelados()));
    }

    @Test
    void deveIdentificarCampanhaSemCancelamentos() {

        when(campanhaRepository.existsById(1L))
                .thenReturn(true);

        when(campanhaTituloRepository.contarTitulosPorCampanha(1L))
                .thenReturn(2L);

        when(campanhaTituloRepository.contarTitulosCanceladosPorCampanha(1L))
                .thenReturn(0L);

        var resultado = service.consultar(1L);

        assertEquals(2, resultado.totalTitulos());
        assertEquals(0, resultado.titulosCancelados());
        assertEquals(2, resultado.naoEncontradosNosPdfs());
        assertEquals(0, resultado.percentualCancelados()
                .compareTo(BigDecimal.ZERO));
    }

    @Test
    void deveCalcularPercentualParcial() {

        when(campanhaRepository.existsById(4L))
                .thenReturn(true);

        when(campanhaTituloRepository.contarTitulosPorCampanha(4L))
                .thenReturn(3L);

        when(campanhaTituloRepository.contarTitulosCanceladosPorCampanha(4L))
                .thenReturn(1L);

        var resultado = service.consultar(4L);

        assertEquals(3, resultado.totalTitulos());
        assertEquals(1, resultado.titulosCancelados());
        assertEquals(2, resultado.naoEncontradosNosPdfs());

        assertEquals(
                0,
                new BigDecimal("33.33")
                        .compareTo(resultado.percentualCancelados()));
    }

    @Test
    void deveRejeitarCampanhaInexistente() {

        when(campanhaRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.consultar(999L));

        verify(
                campanhaTituloRepository,
                never()).contarTitulosPorCampanha(anyLong());
    }
}
