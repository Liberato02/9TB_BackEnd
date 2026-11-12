
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.Campanha;
import br.com.nonetpsp.backend.entity.CampanhaTitulo;
import br.com.nonetpsp.backend.entity.Titulo;

import br.com.nonetpsp.backend.repository.CampanhaRepository;
import br.com.nonetpsp.backend.repository.CampanhaTituloRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final CampanhaTituloRepository campanhaTituloRepository;

    public CampanhaService(
            CampanhaRepository campanhaRepository,
            CampanhaTituloRepository campanhaTituloRepository) {

        this.campanhaRepository = campanhaRepository;
        this.campanhaTituloRepository = campanhaTituloRepository;
    }

    @Transactional
    public Campanha criarCampanha() {

        Campanha campanha = new Campanha();

        // Nome temporário até o banco gerar o ID
        campanha.setNome("Campanha em criação");

        campanha = campanhaRepository.saveAndFlush(campanha);

        // Nome definitivo baseado no ID único
        campanha.setNome(
                String.format("Campanha %03d", campanha.getId()));

        return campanhaRepository.save(campanha);
    }

    @Transactional
    public boolean associarTitulo(Campanha campanha, Titulo titulo) {

        if (campanha == null || campanha.getId() == null) {
            throw new IllegalArgumentException(
                    "A campanha precisa estar cadastrada.");
        }

        if (titulo == null || titulo.getId() == null) {
            throw new IllegalArgumentException(
                    "O título precisa estar cadastrado.");
        }

        boolean jaAssociado = campanhaTituloRepository.existsByCampanhaIdAndTituloId(
                campanha.getId(),
                titulo.getId());

        if (jaAssociado) {
            return false;
        }

        CampanhaTitulo associacao = new CampanhaTitulo();
        associacao.setCampanha(campanha);
        associacao.setTitulo(titulo);

        campanhaTituloRepository.save(associacao);

        return true;
    }
}
