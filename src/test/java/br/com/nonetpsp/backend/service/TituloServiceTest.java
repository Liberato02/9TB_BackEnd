
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;
import br.com.nonetpsp.backend.entity.Apresentante;
import br.com.nonetpsp.backend.entity.Titulo;
import br.com.nonetpsp.backend.repository.TituloRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

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

        when(tituloRepository
                .existsByNumeroProtocoloAndDataProtocolo(
                        "617",
                        LocalDate.of(2026, 6, 10)))
                .thenReturn(false);

        when(importacaoCsvService.identificarApresentante(registro))
                .thenReturn(new Apresentante());

        boolean resultado = tituloService.importarTitulo(registro);

        assertTrue(resultado);

        verify(tituloRepository).save(any(Titulo.class));
    }

    @Test
    void naoDeveSalvarTituloDuplicado() {

        when(tituloRepository
                .existsByNumeroProtocoloAndDataProtocolo(
                        "617",
                        LocalDate.of(2026, 6, 10)))
                .thenReturn(true);

        boolean resultado = tituloService.importarTitulo(registro);

        assertFalse(resultado);

        verify(tituloRepository, never()).save(any(Titulo.class));
        verifyNoInteractions(importacaoCsvService);
    }
}
