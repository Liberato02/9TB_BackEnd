
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;
import br.com.nonetpsp.backend.entity.Campanha;
import br.com.nonetpsp.backend.entity.Titulo;
import br.com.nonetpsp.backend.repository.TituloRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    private final CampanhaService campanhaService;
    private final TituloRepository tituloRepository;

    public ImportadorTitulosCsvService(
            LeitorCsvService leitorCsvService,
            InterpretadorCsvService interpretadorCsvService,
            TituloService tituloService,
            SeparadorCsvService separadorCsvService,
            CampanhaService campanhaService,
            TituloRepository tituloRepository) {

        this.leitorCsvService = leitorCsvService;
        this.interpretadorCsvService = interpretadorCsvService;
        this.tituloService = tituloService;
        this.separadorCsvService = separadorCsvService;
        this.campanhaService = campanhaService;
        this.tituloRepository = tituloRepository;
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

        // Interpreta os registros antes de criar a campanha
        List<ImportacaoCsvDTO> registros = new ArrayList<>();

        for (int i = 1; i < linhas.size(); i++) {
            registros.add(
                    interpretadorCsvService.interpretarLinha(linhas.get(i)));
        }

        Campanha campanha = campanhaService.criarCampanha();

        int titulosNovos = 0;
        int titulosExistentes = 0;
        int titulosAssociados = 0;
        int duplicadosNoCsv = 0;

        Set<String> protocolosProcessados = new HashSet<>();

        for (ImportacaoCsvDTO registro : registros) {

            // Remove zeros à esquerda apenas para comparação
            String numeroNormalizado = normalizarProtocolo(
                    registro.numeroProtocolo());

            String chave = numeroNormalizado
                    + "|" + registro.dataProtocolo();

            if (!protocolosProcessados.add(chave)) {
                duplicadosNoCsv++;
                continue;
            }

            // Verifica se o título já existe, independentemente
            // dos zeros à esquerda
            boolean jaExiste = tituloRepository
                    .buscarPorProtocoloNormalizado(
                            registro.numeroProtocolo(),
                            registro.dataProtocolo())
                    .isPresent();

            Titulo titulo = tituloService.buscarOuCriarTitulo(registro);

            if (jaExiste) {
                titulosExistentes++;
            } else {
                titulosNovos++;
            }

            boolean associado = campanhaService.associarTitulo(
                    campanha, titulo);

            if (associado) {
                titulosAssociados++;
            }
        }

        return new ResultadoImportacao(
                campanha.getId(),
                campanha.getNome(),
                titulosNovos,
                titulosExistentes,
                titulosAssociados,
                duplicadosNoCsv);
    }

    private String normalizarProtocolo(String numero) {

        String normalizado = numero.replaceFirst("^0+(?!$)", "");

        return normalizado;
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
            Long campanhaId,
            String campanhaNome,
            int titulosNovos,
            int titulosExistentes,
            int titulosAssociados,
            int duplicadosNoCsv) {
    }
}
