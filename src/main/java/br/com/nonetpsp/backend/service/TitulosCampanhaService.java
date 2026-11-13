
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.Cancelamento;
import br.com.nonetpsp.backend.entity.Titulo;
import br.com.nonetpsp.backend.repository.CampanhaRepository;
import br.com.nonetpsp.backend.repository.CampanhaTituloRepository;
import br.com.nonetpsp.backend.repository.CancelamentoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TitulosCampanhaService {

    private final CampanhaRepository campanhaRepository;
    private final CampanhaTituloRepository campanhaTituloRepository;
    private final CancelamentoRepository cancelamentoRepository;

    public TitulosCampanhaService(
            CampanhaRepository campanhaRepository,
            CampanhaTituloRepository campanhaTituloRepository,
            CancelamentoRepository cancelamentoRepository) {

        this.campanhaRepository = campanhaRepository;
        this.campanhaTituloRepository = campanhaTituloRepository;
        this.cancelamentoRepository = cancelamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<TituloCampanhaDTO> listar(Long campanhaId) {

        if (!campanhaRepository.existsById(campanhaId)) {
            throw new IllegalArgumentException(
                    "Campanha não encontrada: " + campanhaId);
        }

        Map<String, LocalDate> cancelamentos = cancelamentoRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        c -> chave(
                                c.getNumeroProtocolo(),
                                c.getDataProtocolo()),
                        Cancelamento::getDataCancelamento,
                        (primeira, segunda) -> primeira.isBefore(segunda)
                                ? primeira
                                : segunda));

        return campanhaTituloRepository
                .listarTitulosPorCampanha(campanhaId)
                .stream()
                .map(associacao -> {

                    Titulo titulo = associacao.getTitulo();

                    LocalDate dataCancelamento = cancelamentos.get(
                            chave(
                                    titulo.getNumeroProtocolo(),
                                    titulo.getDataProtocolo()));

                    boolean encontrado = dataCancelamento != null;

                    return new TituloCampanhaDTO(
                            titulo.getId(),
                            titulo.getNumeroProtocolo(),
                            titulo.getDataProtocolo(),
                            titulo.getApresentante().getNome(),
                            titulo.getValorTitulo(),
                            titulo.getValorCustas(),
                            encontrado,
                            dataCancelamento,
                            encontrado
                                    ? "ENCONTRADO_NO_PDF"
                                    : "NAO_ENCONTRADO_NO_PDF");
                })
                .toList();
    }

    private String chave(
            String numeroProtocolo,
            LocalDate dataProtocolo) {

        String numeroNormalizado = numeroProtocolo.trim()
                .replaceFirst("^0+(?!$)", "");

        return numeroNormalizado + "|" + dataProtocolo;
    }

    public record TituloCampanhaDTO(
            Long tituloId,
            String numeroProtocolo,
            LocalDate dataProtocolo,
            String apresentante,
            BigDecimal valorTitulo,
            BigDecimal valorCustas,
            boolean encontradoNoPdf,
            LocalDate dataCancelamento,
            String situacao) {
    }
}
