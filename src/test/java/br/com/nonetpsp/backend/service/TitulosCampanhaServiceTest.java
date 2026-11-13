
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.Apresentante;
import br.com.nonetpsp.backend.entity.CampanhaTitulo;
import br.com.nonetpsp.backend.entity.Cancelamento;
import br.com.nonetpsp.backend.entity.Titulo;

import br.com.nonetpsp.backend.repository.CampanhaRepository;
import br.com.nonetpsp.backend.repository.CampanhaTituloRepository;
import br.com.nonetpsp.backend.repository.CancelamentoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TitulosCampanhaServiceTest {

    private CampanhaRepository campanhaRepository;
    private CampanhaTituloRepository campanhaTituloRepository;
    private CancelamentoRepository cancelamentoRepository;

    private TitulosCampanhaService service;

    @BeforeEach
    void configurar() {

        campanhaRepository = mock(CampanhaRepository.class);
        campanhaTituloRepository = mock(CampanhaTituloRepository.class);
        cancelamentoRepository = mock(CancelamentoRepository.class);

        service = new TitulosCampanhaService(
                campanhaRepository,
                campanhaTituloRepository,
                cancelamentoRepository);
    }

    @Test
    void deveIdentificarTituloCancelado() {

        LocalDate dataProtocolo = LocalDate.of(2026, 8, 3);
        LocalDate dataCancelamento = LocalDate.of(2026, 11, 3);

        CampanhaTitulo associacao = criarAssociacao("0652", dataProtocolo);

        Cancelamento cancelamento = criarCancelamento(
                "0652",
                dataProtocolo,
                dataCancelamento);

        when(campanhaRepository.existsById(3L))
                .thenReturn(true);

        when(campanhaTituloRepository.listarTitulosPorCampanha(3L))
                .thenReturn(List.of(associacao));

        when(cancelamentoRepository.buscarCancelamentosPorCampanha(3L))
                .thenReturn(List.of(cancelamento));

        var resultado = service.listar(3L);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).encontradoNoPdf());
        assertEquals(
                dataCancelamento,
                resultado.get(0).dataCancelamento());
        assertEquals(
                "ENCONTRADO_NO_PDF",
                resultado.get(0).situacao());
    }

    @Test
    void deveReconhecerProtocoloComZerosAEsquerda() {

        LocalDate data = LocalDate.of(2026, 8, 3);

        CampanhaTitulo associacao = criarAssociacao("652", data);

        Cancelamento cancelamento = criarCancelamento(
                "0652",
                data,
                LocalDate.of(2026, 11, 3));

        when(campanhaRepository.existsById(3L))
                .thenReturn(true);

        when(campanhaTituloRepository.listarTitulosPorCampanha(3L))
                .thenReturn(List.of(associacao));

        when(cancelamentoRepository.buscarCancelamentosPorCampanha(3L))
                .thenReturn(List.of(cancelamento));

        var resultado = service.listar(3L);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).encontradoNoPdf());
    }

    @Test
    void naoDeveConfundirProtocolosComDatasDiferentes() {

        LocalDate dataTitulo = LocalDate.of(2026, 8, 3);
        LocalDate outraData = LocalDate.of(2026, 7, 21);

        CampanhaTitulo associacao = criarAssociacao("0652", dataTitulo);

        Cancelamento cancelamento = criarCancelamento(
                "0652",
                outraData,
                LocalDate.of(2026, 11, 3));

        when(campanhaRepository.existsById(3L))
                .thenReturn(true);

        when(campanhaTituloRepository.listarTitulosPorCampanha(3L))
                .thenReturn(List.of(associacao));

        when(cancelamentoRepository.buscarCancelamentosPorCampanha(3L))
                .thenReturn(List.of(cancelamento));

        var resultado = service.listar(3L);

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).encontradoNoPdf());
        assertNull(resultado.get(0).dataCancelamento());
        assertEquals(
                "NAO_ENCONTRADO_NO_PDF",
                resultado.get(0).situacao());
    }

    @Test
    void deveRejeitarCampanhaInexistente() {

        when(campanhaRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.listar(999L));

        verifyNoInteractions(
                campanhaTituloRepository,
                cancelamentoRepository);
    }

    private CampanhaTitulo criarAssociacao(
            String protocolo,
            LocalDate dataProtocolo) {

        CampanhaTitulo associacao = mock(CampanhaTitulo.class);
        Titulo titulo = mock(Titulo.class);
        Apresentante apresentante = mock(Apresentante.class);

        when(associacao.getTitulo()).thenReturn(titulo);

        when(titulo.getId()).thenReturn(3L);
        when(titulo.getNumeroProtocolo()).thenReturn(protocolo);
        when(titulo.getDataProtocolo()).thenReturn(dataProtocolo);
        when(titulo.getApresentante()).thenReturn(apresentante);

        when(titulo.getValorTitulo())
                .thenReturn(new BigDecimal("546.73"));

        when(titulo.getValorCustas())
                .thenReturn(new BigDecimal("150.50"));

        when(apresentante.getNome())
                .thenReturn("Apresentante Teste");

        return associacao;
    }

    private Cancelamento criarCancelamento(
            String protocolo,
            LocalDate dataProtocolo,
            LocalDate dataCancelamento) {

        Cancelamento cancelamento = mock(Cancelamento.class);

        when(cancelamento.getNumeroProtocolo())
                .thenReturn(protocolo);

        when(cancelamento.getDataProtocolo())
                .thenReturn(dataProtocolo);

        when(cancelamento.getDataCancelamento())
                .thenReturn(dataCancelamento);

        return cancelamento;
    }
}
