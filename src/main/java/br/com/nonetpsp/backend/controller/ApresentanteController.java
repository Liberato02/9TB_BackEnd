
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.entity.Apresentante;
import br.com.nonetpsp.backend.service.ApresentanteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/apresentantes")
public class ApresentanteController {

    private final ApresentanteService service;

    public ApresentanteController(ApresentanteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Apresentante> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Apresentante> cadastrar(
            @RequestBody Map<String, String> dados) {

        Apresentante apresentante = service.cadastrar(dados.get("nome"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apresentante);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarErro(
            IllegalArgumentException erro) {

        HttpStatus status = erro.getMessage().contains("já está cadastrado")
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(Map.of("erro", erro.getMessage()));
    }
}
