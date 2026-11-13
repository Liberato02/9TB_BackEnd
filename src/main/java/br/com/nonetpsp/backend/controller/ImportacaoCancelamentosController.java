
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.service.ImportacaoCancelamentosService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

@RestController
@RequestMapping("/api/importacoes/cancelamentos")
public class ImportacaoCancelamentosController {

    private static final byte[] ASSINATURA_PDF = "%PDF-".getBytes(StandardCharsets.US_ASCII);

    private final ImportacaoCancelamentosService service;

    public ImportacaoCancelamentosController(
            ImportacaoCancelamentosService service) {
        this.service = service;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> importar(
            @RequestParam("arquivo") MultipartFile arquivo) {

        if (arquivo.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("O arquivo enviado está vazio.");
        }

        String nomeArquivo = arquivo.getOriginalFilename();

        if (nomeArquivo == null
                || !nomeArquivo.toLowerCase(Locale.ROOT)
                        .endsWith(".pdf")) {

            return ResponseEntity.badRequest()
                    .body("Envie um arquivo com extensão .pdf.");
        }

        try (InputStream entrada = new BufferedInputStream(
                arquivo.getInputStream())) {

            entrada.mark(ASSINATURA_PDF.length);

            byte[] cabecalho = entrada.readNBytes(ASSINATURA_PDF.length);

            if (!Arrays.equals(cabecalho, ASSINATURA_PDF)) {
                return ResponseEntity.badRequest()
                        .body("O arquivo não possui um formato PDF válido.");
            }

            entrada.reset();

            try {
                return ResponseEntity.ok(
                        service.importar(entrada));

            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(e.getMessage());

            } catch (IOException e) {
                return ResponseEntity.badRequest()
                        .body("Não foi possível processar o PDF. "
                                + "Verifique se o arquivo está corrompido.");
            }

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao acessar o arquivo enviado.");
        }
    }
}
