
package br.com.nonetpsp.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class LeitorCancelamentosPdfService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private static final Pattern SECAO_CANCELAMENTO = Pattern.compile(
            "Protestos\\s+Cancelados\\s+em\\s*:\\s*" +
                    "(\\d{2}/\\d{2}/\\d{4})",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern PROTOCOLO = Pattern.compile(
            "(?<!\\d)(\\d{4})\\s*-\\s*" +
                    "(\\d{2}/\\d{2}/\\d{4})(?!\\d)");

    public List<RegistroCancelamento> ler(
            InputStream arquivo) throws IOException {

        List<RegistroCancelamento> registros = new ArrayList<>();

        try (PDDocument documento = Loader.loadPDF(arquivo.readAllBytes())) {

            PDFTextStripper extrator = new PDFTextStripper();
            extrator.setSortByPosition(true);

            String texto = extrator.getText(documento);

            Matcher secoes = SECAO_CANCELAMENTO.matcher(texto);

            LocalDate dataCancelamento = null;
            int inicio = 0;

            while (secoes.find()) {

                if (dataCancelamento != null) {
                    extrairProtocolos(
                            texto.substring(inicio, secoes.start()),
                            dataCancelamento,
                            registros);
                }

                dataCancelamento = LocalDate.parse(
                        secoes.group(1),
                        FORMATO_DATA);

                inicio = secoes.end();
            }

            if (dataCancelamento == null) {
                throw new IllegalArgumentException(
                        "Nenhuma seção de cancelamentos encontrada no PDF.");
            }

            extrairProtocolos(
                    texto.substring(inicio),
                    dataCancelamento,
                    registros);
        }

        if (registros.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhum protocolo encontrado no PDF.");
        }

        return registros;
    }

    private void extrairProtocolos(
            String texto,
            LocalDate dataCancelamento,
            List<RegistroCancelamento> registros) {
        Matcher protocolos = PROTOCOLO.matcher(texto);

        while (protocolos.find()) {

            String numeroProtocolo = protocolos.group(1);

            LocalDate dataProtocolo = LocalDate.parse(
                    protocolos.group(2),
                    FORMATO_DATA);

            registros.add(
                    new RegistroCancelamento(
                            numeroProtocolo,
                            dataProtocolo,
                            dataCancelamento));
        }
    }

    public record RegistroCancelamento(
            String numeroProtocolo,
            LocalDate dataProtocolo,
            LocalDate dataCancelamento) {
    }
}
