
package br.com.nonetpsp.backend.service;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeitorCancelamentosPdfServiceTest {

    @Test
    void deveLer359Cancelamentos() throws Exception {

        Path arquivo = Path.of("cancelamentosfeitos.pdf");

        LeitorCancelamentosPdfService leitor = new LeitorCancelamentosPdfService();

        try (InputStream entrada = Files.newInputStream(arquivo)) {

            var registros = leitor.ler(entrada);

            System.out.println("\n===== RESULTADO DO PDF =====");
            System.out.println("Total extraído: " + registros.size());

            Map<Object, Long> porData = registros.stream()
                    .collect(Collectors.groupingBy(
                            r -> r.dataCancelamento(),
                            TreeMap::new,
                            Collectors.counting()));

            porData.forEach((data, quantidade) -> System.out.println(data + ": " + quantidade));

            System.out.println("============================\n");

            assertEquals(
                    359,
                    registros.size(),
                    "O PDF deve conter 359 cancelamentos.");
        }
    }
}
