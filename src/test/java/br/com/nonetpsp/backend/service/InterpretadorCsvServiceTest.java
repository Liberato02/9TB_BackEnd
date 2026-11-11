
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InterpretadorCsvServiceTest {

    @Test
    void deveInterpretarLinhaCsvCorretamente() {

        ProtocoloCsvService protocoloService = new ProtocoloCsvService();

        SeparadorCsvService separadorService = new SeparadorCsvService();

        InterpretadorCsvService interpretador = new InterpretadorCsvService(
                protocoloService,
                separadorService);

        String linha = "617 - 10/06/2026;TIPO;123;CLIENTE;"
                + "R$ 150,50;R$ 1.200,00;"
                + "11999999999;teste@exemplo.com;"
                + "Enel;EMAIL;TELEFONE";

        ImportacaoCsvDTO resultado = interpretador.interpretarLinha(linha);

        assertEquals("617", resultado.numeroProtocolo());

        assertEquals(
                LocalDate.of(2026, 6, 10),
                resultado.dataProtocolo());

        assertEquals("Enel", resultado.apresentante());

        assertEquals(
                new BigDecimal("150.50"),
                resultado.valorCustas());

        assertEquals(
                new BigDecimal("1200.00"),
                resultado.valorTitulo());
    }
}
