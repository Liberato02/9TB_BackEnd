
package br.com.nonetpsp.backend.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class LeitorCancelamentosPdfServiceTest {

        private final LeitorCancelamentosPdfService leitor = new LeitorCancelamentosPdfService();

        @Test
        void deveLerCancelamentosDePdfFicticio() throws Exception {

                byte[] pdf = criarPdfFicticio(
                                "Protestos Cancelados em: 03/11/2026",
                                "0652 - 03/08/2026",
                                "1561 - 04/09/2026",
                                "Protestos Cancelados em: 04/11/2026",
                                "0701 - 05/09/2026");

                try (InputStream entrada = new ByteArrayInputStream(pdf)) {

                        var registros = leitor.ler(entrada);

                        assertEquals(3, registros.size());

                        assertEquals(
                                        new LeitorCancelamentosPdfService.RegistroCancelamento(
                                                        "0652",
                                                        LocalDate.of(2026, 8, 3),
                                                        LocalDate.of(2026, 11, 3)),
                                        registros.get(0));

                        assertEquals(
                                        new LeitorCancelamentosPdfService.RegistroCancelamento(
                                                        "1561",
                                                        LocalDate.of(2026, 9, 4),
                                                        LocalDate.of(2026, 11, 3)),
                                        registros.get(1));

                        assertEquals(
                                        new LeitorCancelamentosPdfService.RegistroCancelamento(
                                                        "0701",
                                                        LocalDate.of(2026, 9, 5),
                                                        LocalDate.of(2026, 11, 4)),
                                        registros.get(2));
                }
        }

        @Test
        void deveRejeitarPdfSemSecaoDeCancelamentos() throws Exception {

                byte[] pdf = criarPdfFicticio(
                                "Relatorio de consulta",
                                "Nenhum cancelamento informado");

                try (InputStream entrada = new ByteArrayInputStream(pdf)) {

                        IllegalArgumentException erro = assertThrows(
                                        IllegalArgumentException.class,
                                        () -> leitor.ler(entrada));

                        assertEquals(
                                        "Nenhuma seção de cancelamentos encontrada no PDF.",
                                        erro.getMessage());
                }
        }

        @Test
        void deveLer359CancelamentosDePdfFicticioComMultiplasPaginas()
                        throws Exception {

                List<String> linhas = new ArrayList<>();

                List<LeitorCancelamentosPdfService.RegistroCancelamento> esperados = new ArrayList<>();

                linhas.add("Protestos Cancelados em: 03/11/2026");

                for (int i = 1; i <= 359; i++) {

                        if (i == 180) {
                                linhas.add("Protestos Cancelados em: 04/11/2026");
                        }

                        String protocolo = String.format(Locale.ROOT, "%04d", i);

                        LocalDate dataProtocolo = LocalDate.of(2026, 8, 3);

                        LocalDate dataCancelamento = i < 180
                                        ? LocalDate.of(2026, 11, 3)
                                        : LocalDate.of(2026, 11, 4);

                        linhas.add(protocolo + " - 03/08/2026");

                        esperados.add(
                                        new LeitorCancelamentosPdfService.RegistroCancelamento(
                                                        protocolo,
                                                        dataProtocolo,
                                                        dataCancelamento));
                }

                byte[] pdf = criarPdfFicticio(linhas.toArray(String[]::new));

                try (InputStream entrada = new ByteArrayInputStream(pdf)) {

                        var registros = leitor.ler(entrada);

                        assertEquals(359, registros.size());

                        assertEquals(esperados, registros);
                }
        }

        @Test
        void deveLer359CancelamentosSePdfRealEstiverDisponivel()
                        throws Exception {

                Path arquivo = Path.of("cancelamentosfeitos.pdf");

                assumeTrue(
                                Files.isRegularFile(arquivo),
                                "PDF real não disponível neste ambiente.");

                try (InputStream entrada = Files.newInputStream(arquivo)) {

                        var registros = leitor.ler(entrada);

                        assertEquals(
                                        359,
                                        registros.size(),
                                        "O PDF real deve conter 359 cancelamentos.");
                }
        }

        private byte[] criarPdfFicticio(String... linhas)
                        throws Exception {

                try (PDDocument documento = new PDDocument();
                                ByteArrayOutputStream saida = new ByteArrayOutputStream()) {

                        int linhasPorPagina = 35;

                        for (int inicio = 0; inicio < linhas.length; inicio += linhasPorPagina) {

                                PDPage pagina = new PDPage();
                                documento.addPage(pagina);

                                int fim = Math.min(
                                                inicio + linhasPorPagina,
                                                linhas.length);

                                try (PDPageContentStream conteudo = new PDPageContentStream(documento, pagina)) {

                                        conteudo.beginText();

                                        conteudo.setFont(
                                                        new PDType1Font(
                                                                        Standard14Fonts.FontName.HELVETICA),
                                                        12);

                                        conteudo.newLineAtOffset(50, 750);

                                        for (int i = inicio; i < fim; i++) {

                                                conteudo.showText(linhas[i]);
                                                conteudo.newLineAtOffset(0, -20);
                                        }

                                        conteudo.endText();
                                }
                        }

                        documento.save(saida);

                        return saida.toByteArray();
                }
        }
}
