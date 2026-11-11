
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InterpretadorCsvService {

    private final ProtocoloCsvService protocoloService;
    private final SeparadorCsvService separadorService;

    public InterpretadorCsvService(
            ProtocoloCsvService protocoloService,
            SeparadorCsvService separadorService) {

        this.protocoloService = protocoloService;
        this.separadorService = separadorService;
    }

    public ImportacaoCsvDTO interpretarLinha(String linha) {

        String[] colunas = separadorService.separar(linha);

        return interpretar(colunas);
    }

    public ImportacaoCsvDTO interpretar(String[] colunas) {

        if (colunas == null || colunas.length != 11) {
            throw new IllegalArgumentException(
                    "O CSV deve conter exatamente 11 colunas.");
        }

        ProtocoloCsvService.Protocolo protocolo = protocoloService.interpretar(colunas[0].trim());

        String apresentante = colunas[8].trim();

        if (apresentante.isBlank()) {
            throw new IllegalArgumentException(
                    "O apresentante é obrigatório.");
        }

        BigDecimal custas = converterValor(colunas[4]);
        BigDecimal valorTitulo = converterValor(colunas[5]);

        return new ImportacaoCsvDTO(
                apresentante,
                protocolo.numero(),
                protocolo.data(),
                custas,
                valorTitulo);
    }

    private BigDecimal converterValor(String texto) {

        if (texto == null || texto.isBlank()) {
            return null;
        }

        String valor = texto.trim()
                .replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim();

        try {
            return new BigDecimal(valor);

        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException(
                    "Valor monetário inválido: " + texto);
        }
    }
}
