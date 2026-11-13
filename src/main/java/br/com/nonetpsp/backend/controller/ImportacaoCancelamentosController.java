
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.service.ImportacaoCancelamentosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/importacoes/cancelamentos")
public class ImportacaoCancelamentosController {

    private final ImportacaoCancelamentosService service;

    public ImportacaoCancelamentosController(
            ImportacaoCancelamentosService service) {
        this.service = service;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> importar(
            @RequestParam("arquivo") MultipartFile arquivo) throws IOException {

        if (arquivo.isEmpty()
                || arquivo.getOriginalFilename() == null
                || !arquivo.getOriginalFilename().toLowerCase()
                        .endsWith(".pdf")) {

            return ResponseEntity.badRequest()
                    .body("Envie um arquivo PDF válido.");
        }

        try {
            return ResponseEntity.ok(
                    service.importar(arquivo.getInputStream()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
