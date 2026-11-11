
package br.com.nonetpsp.backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ProtocoloCsvService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    public record Protocolo(
            String numero,
            LocalDate data) {
    }

    public Protocolo interpretar(String texto) {

        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(
                    "O protocolo é obrigatório.");
        }

        String[] partes = texto.split("\\s+-\\s+", -1);

        if (partes.length != 2) {
            throw new IllegalArgumentException(
                    "Formato de protocolo inválido: " + texto);
        }

        String numero = partes[0].trim();
        String dataTexto = partes[1].trim();

        if (!numero.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "Número de protocolo inválido: " + numero);
        }

        try {
            LocalDate data = LocalDate.parse(
                    dataTexto,
                    FORMATO_DATA);

            return new Protocolo(numero, data);

        } catch (DateTimeParseException erro) {
            throw new IllegalArgumentException(
                    "Data de protocolo inválida: " + dataTexto);
        }
    }
}
