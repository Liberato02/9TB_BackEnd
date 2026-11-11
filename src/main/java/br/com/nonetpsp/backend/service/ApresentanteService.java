
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.Apresentante;
import br.com.nonetpsp.backend.entity.ApresentanteAlias;
import br.com.nonetpsp.backend.repository.ApresentanteRepository;
import br.com.nonetpsp.backend.repository.ApresentanteAliasRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApresentanteService {

    private final ApresentanteRepository repository;
    private final ApresentanteAliasRepository aliasRepository;

    public ApresentanteService(
            ApresentanteRepository repository,
            ApresentanteAliasRepository aliasRepository) {
        this.repository = repository;
        this.aliasRepository = aliasRepository;
    }

    public List<Apresentante> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Apresentante> buscarPorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            return Optional.empty();
        }

        String nomeTratado = nome.trim();

        Optional<Apresentante> principal = repository.findByNomeIgnoreCase(nomeTratado);

        if (principal.isPresent()) {
            return principal;
        }

        return aliasRepository
                .findByNomeAlternativoIgnoreCase(nomeTratado)
                .map(ApresentanteAlias::getApresentante);
    }

    @Transactional
    public Apresentante cadastrar(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do apresentante é obrigatório.");
        }

        String nomeTratado = nome.trim();

        if (buscarPorNome(nomeTratado).isPresent()) {
            throw new IllegalArgumentException(
                    "Este apresentante já está cadastrado.");
        }

        Apresentante apresentante = new Apresentante();
        apresentante.setNome(nomeTratado);

        return repository.save(apresentante);
    }
}
