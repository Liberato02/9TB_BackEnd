
package br.com.nonetpsp.backend.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeparadorCsvService {

    public String[] separar(String linha) {

        if (linha == null) {
            throw new IllegalArgumentException(
                    "A linha do CSV não pode ser nula.");
        }

        List<String> colunas = new ArrayList<>();
        StringBuilder campo = new StringBuilder();

        boolean entreAspas = false;

        for (int i = 0; i < linha.length(); i++) {

            char caractere = linha.charAt(i);

            if (caractere == '"') {

                if (entreAspas
                        && i + 1 < linha.length()
                        && linha.charAt(i + 1) == '"') {

                    campo.append('"');
                    i++;

                } else {
                    entreAspas = !entreAspas;
                }

            } else if (caractere == ';' && !entreAspas) {

                colunas.add(campo.toString());
                campo.setLength(0);

            } else {
                campo.append(caractere);
            }
        }

        if (entreAspas) {
            throw new IllegalArgumentException(
                    "Campo CSV com aspas não fechadas.");
        }

        colunas.add(campo.toString());

        return colunas.toArray(new String[0]);
    }
}
