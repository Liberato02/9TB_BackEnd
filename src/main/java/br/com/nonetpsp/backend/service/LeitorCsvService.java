
package br.com.nonetpsp.backend.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeitorCsvService {

    public List<String> lerLinhas(InputStream arquivo)
            throws IOException {

        List<String> linhas = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(
                        arquivo,
                        StandardCharsets.UTF_8))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                if (!linha.isBlank()) {
                    linhas.add(linha);
                }
            }
        }

        return linhas;
    }
}
