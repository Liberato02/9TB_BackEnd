
package br.com.nonetpsp.backend.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProtocoloCsvServiceTest {

    private final ProtocoloCsvService service = new ProtocoloCsvService();

    @Test
    void deveAceitarDataValida() {

        var resultado = service.interpretar(
                "0652 - 03/08/2026");

        assertEquals("0652", resultado.numero());
        assertEquals(
                LocalDate.of(2026, 8, 3),
                resultado.data());
    }

    @Test
    void deveRejeitarDataImpossivel() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.interpretar(
                        "652 - 31/02/2026"));
    }

    @Test
    void deveRejeitarDia29EmAnoNaoBissexto() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.interpretar(
                        "652 - 29/02/2026"));
    }

    @Test
    void deveAceitarDia29EmAnoBissexto() {

        var resultado = service.interpretar(
                "652 - 29/02/2024");

        assertEquals(
                LocalDate.of(2024, 2, 29),
                resultado.data());
    }
}
