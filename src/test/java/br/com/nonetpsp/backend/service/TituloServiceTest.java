
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;
import br.com.nonetpsp.backend.entity.Apresentante;
import br.com.nonetpsp.backend.entity.Titulo;
import br.com.nonetpsp.backend.repository.TituloRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TituloServiceTest {

        private TituloRepository tituloRepository;
        private ImportacaoCsvService importacaoCsvService;
        private TituloService tituloService;

        private ImportacaoCsvDTO registro;

        @BeforeEach
        void preparar() {

                tituloRepository = mock(TituloRepository.class);
                importacaoCsvService = mock(ImportacaoCsvService.class);

                tituloService = new TituloService(
                                tituloRepository,
                                importacaoCsvService);

                registro = new ImportacaoCsvDTO(
                                "Enel",
                                "617",
                                LocalDate.of(2026, 6, 10),
                                new BigDecimal("150.50"),
                                new BigDecimal("1200.00"));
        }

        @Test
        void deveSalvarTituloNovo() {

                when(tituloRepository.buscarPorProtocoloNormalizado(
                                "617",
                                LocalDate.of(2026, 6, 10)))
                                .thenReturn(Optional.empty());

                when(importacaoCsvService.identificarApresentante(registro))
                                .thenReturn(new Apresentante());

                boolean resultado = tituloService.importarTitulo(registro);

                assertTrue(resultado);

                verify(tituloRepository).save(any(Titulo.class));
        }

        @Test
        void naoDeveSalvarTituloDuplicado() {

                Titulo tituloExistente = new Titulo();

                when(tituloRepository.buscarPorProtocoloNormalizado(
                                "617",
                                LocalDate.of(2026, 6, 10)))
                                .thenReturn(Optional.of(tituloExistente));

                boolean resultado = tituloService.importarTitulo(registro);

                assertFalse(resultado);

                verify(tituloRepository, never()).save(any(Titulo.class));
                verifyNoInteractions(importacaoCsvService);
        }

        @Test
        void deveReutilizarTituloComZerosAEsquerda() {

                LocalDate data = LocalDate.of(2026, 8, 3);

                ImportacaoCsvDTO registroComZeros = new ImportacaoCsvDTO(
                                "Enel",
                                "0652",
                                data,
                                new BigDecimal("150.50"),
                                new BigDecimal("546.73"));

                Titulo tituloExistente = new Titulo();
                tituloExistente.setNumeroProtocolo("652");
                tituloExistente.setDataProtocolo(data);

                when(tituloRepository.buscarPorProtocoloNormalizado(
                                "0652",
                                data))
                                .thenReturn(Optional.of(tituloExistente));

                Titulo resultado = tituloService.buscarOuCriarTitulo(
                                registroComZeros);

                assertSame(tituloExistente, resultado);

                verify(tituloRepository, never()).save(any(Titulo.class));
                verifyNoInteractions(importacaoCsvService);
        }
}
