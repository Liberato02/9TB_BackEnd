
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.entity.ApresentanteAlias;
import br.com.nonetpsp.backend.service.ApresentanteAliasService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/apresentantes")
public class ApresentanteAliasController {

    private final ApresentanteAliasService service;

    public ApresentanteAliasController(
            ApresentanteAliasService service) {
        this.service = service;
    }

    @PostMapping("/{id}/aliases")
    public ResponseEntity<Map<String, Object>> cadastrar(
            @PathVariable Long id,
            @RequestBody Map<String, String> dados) {

        ApresentanteAlias alias = service.cadastrar(id, dados.get("nome"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", alias.getId(),
                        "nomeAlternativo", alias.getNomeAlternativo(),
                        "apresentanteId", id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarErro(
            IllegalArgumentException erro) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", erro.getMessage()));
    }
}
