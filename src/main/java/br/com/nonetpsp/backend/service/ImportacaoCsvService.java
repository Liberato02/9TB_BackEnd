
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;
import br.com.nonetpsp.backend.entity.Apresentante;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImportacaoCsvService {

    private final ApresentanteService apresentanteService;

    public ImportacaoCsvService(
            ApresentanteService apresentanteService) {
        this.apresentanteService = apresentanteService;
    }

    @Transactional
    public Apresentante identificarApresentante(
            ImportacaoCsvDTO registro) {

        String nome = registro.apresentante();

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O apresentante é obrigatório.");
        }

        String nomeTratado = nome.trim();

        return apresentanteService
                .buscarPorNome(nomeTratado)
                .orElseGet(() -> apresentanteService.cadastrar(nomeTratado));
    }
}
