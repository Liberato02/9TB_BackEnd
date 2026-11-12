
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@Service
public class ImportadorTitulosCsvService {

    private static final List<String> CABECALHO_ESPERADO = List.of(
            "PROTOCOLO",
            "TIPO DE DOCUMENTO",
            "NÚMERO DE DOCUMENTO",
            "NOME",
            "VALOR DE CUSTAS",
            "VALOR DO TÍTULO",
            "TELEFONE",
            "EMAIL",
            "APRESENTANTE",
            "ORIGEM EMAIL",
            "ORIGEM TELEFONE");

    private final LeitorCsvService leitorCsvService;
    private final InterpretadorCsvService interpretadorCsvService;
    private final TituloService tituloService;
    private final SeparadorCsvService separadorCsvService;

    public ImportadorTitulosCsvService(
            LeitorCsvService leitorCsvService,
            InterpretadorCsvService interpretadorCsvService,
            TituloService tituloService,
            SeparadorCsvService separadorCsvService) {

        this.leitorCsvService = leitorCsvService;
        this.interpretadorCsvService = interpretadorCsvService;
        this.tituloService = tituloService;
        this.separadorCsvService = separadorCsvService;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultadoImportacao importar(InputStream arquivo)
            throws IOException {

        List<String> linhas = leitorCsvService.lerLinhas(arquivo);

        if (linhas.isEmpty()) {
            throw new IllegalArgumentException(
                    "O arquivo CSV está vazio.");
        }

        validarCabecalho(linhas.get(0));

        if (linhas.size() == 1) {
            throw new IllegalArgumentException(
                    "O arquivo CSV não possui registros.");
        }

        int importados = 0;
        int duplicados = 0;

        for (int i = 1; i < linhas.size(); i++) {

            String linha = linhas.get(i);

            ImportacaoCsvDTO registro = interpretadorCsvService.interpretarLinha(linha);

            boolean importado = tituloService.importarTitulo(registro);

            if (importado) {
                importados++;
            } else {
                duplicados++;
            }
        }

        return new ResultadoImportacao(
                importados,
                duplicados);
    }

    private void validarCabecalho(String linha) {

        String[] colunas = separadorCsvService.separar(linha);

        if (colunas.length != CABECALHO_ESPERADO.size()) {
            throw new IllegalArgumentException(
                    "Cabeçalho inválido: o CSV deve conter 11 colunas.");
        }

        for (int i = 0; i < colunas.length; i++) {

            String coluna = colunas[i]
                    .replace("\uFEFF", "")
                    .trim()
                    .toUpperCase(Locale.ROOT);

            if (!coluna.equals(CABECALHO_ESPERADO.get(i))) {
                throw new IllegalArgumentException(
                        "Cabeçalho inválido na coluna " + (i + 1)
                                + ". Esperado: " + CABECALHO_ESPERADO.get(i));
            }
        }
    }

    public record ResultadoImportacao(
            int importados,
            int duplicados) {
    }
}
