
package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.entity.Campanha;
import br.com.nonetpsp.backend.repository.CampanhaRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/campanhas")
public class CampanhaController {

    private final CampanhaRepository repository;

    public CampanhaController(CampanhaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<CampanhaResumo> listar() {
        return repository.findAll(
                Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(this::converter)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaResumo> buscar(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(this::converter)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private CampanhaResumo converter(Campanha campanha) {
        return new CampanhaResumo(
                campanha.getId(),
                campanha.getNome(),
                campanha.getDataCriacao());
    }

    public record CampanhaResumo(
            Long id,
            String nome,
            LocalDateTime dataCriacao) {
    }
}
