
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.service.ImportadorTitulosCsvService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/importacoes")
public class ImportacaoCsvController {

    private final ImportadorTitulosCsvService importadorService;

    public ImportacaoCsvController(
            ImportadorTitulosCsvService importadorService) {

        this.importadorService = importadorService;
    }

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarCsv(
            @RequestParam("arquivo") MultipartFile arquivo)
            throws IOException {

        if (arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("erro", "O arquivo CSV está vazio."));
        }

        String nomeArquivo = arquivo.getOriginalFilename();

        if (nomeArquivo == null
                || !nomeArquivo.toLowerCase(Locale.ROOT)
                        .endsWith(".csv")) {

            return ResponseEntity.badRequest().body(
                    Map.of("erro", "Envie um arquivo com extensão .csv."));
        }

        try {

            var resultado = importadorService.importar(
                    arquivo.getInputStream());

            return ResponseEntity.ok(resultado);

        } catch (IllegalArgumentException erro) {

            return ResponseEntity.badRequest().body(
                    Map.of("erro", erro.getMessage()));
        }
    }
}
