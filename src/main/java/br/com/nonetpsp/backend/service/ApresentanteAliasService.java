
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.Apresentante;
import br.com.nonetpsp.backend.entity.ApresentanteAlias;
import br.com.nonetpsp.backend.repository.ApresentanteAliasRepository;
import br.com.nonetpsp.backend.repository.ApresentanteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApresentanteAliasService {

    private final ApresentanteAliasRepository aliasRepository;
    private final ApresentanteRepository apresentanteRepository;

    public ApresentanteAliasService(
            ApresentanteAliasRepository aliasRepository,
            ApresentanteRepository apresentanteRepository) {

        this.aliasRepository = aliasRepository;
        this.apresentanteRepository = apresentanteRepository;
    }

    @Transactional
    public ApresentanteAlias cadastrar(Long apresentanteId, String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome alternativo é obrigatório.");
        }

        String nomeTratado = nome.trim();

        Apresentante apresentante = apresentanteRepository
                .findById(apresentanteId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Apresentante não encontrado."));

        if (aliasRepository.existsByNomeAlternativoIgnoreCase(nomeTratado)
                || apresentanteRepository.existsByNomeIgnoreCase(nomeTratado)) {
            throw new IllegalArgumentException(
                    "Este nome já está cadastrado.");
        }

        ApresentanteAlias alias = new ApresentanteAlias();
        alias.setNomeAlternativo(nomeTratado);
        alias.setApresentante(apresentante);

        return aliasRepository.save(alias);
    }
}
