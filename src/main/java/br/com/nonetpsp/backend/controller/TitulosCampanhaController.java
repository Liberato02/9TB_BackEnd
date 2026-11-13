
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.service.TitulosCampanhaService;
import br.com.nonetpsp.backend.service.TitulosCampanhaService.TituloCampanhaDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/campanhas")
public class TitulosCampanhaController {

    private final TitulosCampanhaService service;

    public TitulosCampanhaController(
            TitulosCampanhaService service) {
        this.service = service;
    }

    @GetMapping("/{id}/titulos")
    public ResponseEntity<List<TituloCampanhaDTO>> listar(
            @PathVariable Long id) {

        try {
            return ResponseEntity.ok(service.listar(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
