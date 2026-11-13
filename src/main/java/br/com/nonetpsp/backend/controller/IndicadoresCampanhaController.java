
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.service.IndicadoresCampanhaService;
import br.com.nonetpsp.backend.service.IndicadoresCampanhaService.IndicadoresCampanha;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campanhas")
public class IndicadoresCampanhaController {

    private final IndicadoresCampanhaService service;

    public IndicadoresCampanhaController(
            IndicadoresCampanhaService service) {
        this.service = service;
    }

    @GetMapping("/{id}/indicadores")
    public ResponseEntity<?> consultar(
            @PathVariable Long id) {

        try {
            IndicadoresCampanha indicadores = service.consultar(id);

            return ResponseEntity.ok(indicadores);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
