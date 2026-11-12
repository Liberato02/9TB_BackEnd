
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.dto.ImportacaoCsvDTO;
import br.com.nonetpsp.backend.entity.Apresentante;
import br.com.nonetpsp.backend.entity.Titulo;
import br.com.nonetpsp.backend.repository.TituloRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TituloService {

    private final TituloRepository tituloRepository;
    private final ImportacaoCsvService importacaoCsvService;

    public TituloService(
            TituloRepository tituloRepository,
            ImportacaoCsvService importacaoCsvService) {

        this.tituloRepository = tituloRepository;
        this.importacaoCsvService = importacaoCsvService;
    }

    @Transactional
    public boolean importarTitulo(ImportacaoCsvDTO registro) {

        boolean jaExiste = tituloRepository.existsByNumeroProtocoloAndDataProtocolo(
                registro.numeroProtocolo(),
                registro.dataProtocolo());

        if (jaExiste) {
            return false;
        }

        Apresentante apresentante = importacaoCsvService.identificarApresentante(registro);

        Titulo titulo = new Titulo();

        titulo.setNumeroProtocolo(registro.numeroProtocolo());
        titulo.setDataProtocolo(registro.dataProtocolo());
        titulo.setApresentante(apresentante);
        titulo.setValorCustas(registro.valorCustas());
        titulo.setValorTitulo(registro.valorTitulo());

        tituloRepository.save(titulo);

        return true;
    }
}
