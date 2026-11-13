
package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.Cancelamento;
import br.com.nonetpsp.backend.repository.CancelamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ImportacaoCancelamentosService {

    private final LeitorCancelamentosPdfService leitor;
    private final CancelamentoRepository repository;

    public ImportacaoCancelamentosService(
            LeitorCancelamentosPdfService leitor,
            CancelamentoRepository repository) {
        this.leitor = leitor;
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultadoImportacao importar(InputStream arquivo) throws IOException {

        List<LeitorCancelamentosPdfService.RegistroCancelamento> registros = leitor.ler(arquivo);

        int novos = 0;
        int existentes = 0;

        for (var registro : registros) {

            boolean jaExiste = repository.existsByNumeroProtocoloAndDataProtocolo(
                    registro.numeroProtocolo(),
                    registro.dataProtocolo());

            if (jaExiste) {
                existentes++;
                continue;
            }

            Cancelamento cancelamento = new Cancelamento();
            cancelamento.setNumeroProtocolo(registro.numeroProtocolo());
            cancelamento.setDataProtocolo(registro.dataProtocolo());
            cancelamento.setDataCancelamento(registro.dataCancelamento());

            repository.save(cancelamento);
            novos++;
        }

        return new ResultadoImportacao(
                registros.size(),
                novos,
                existentes);
    }

    public record ResultadoImportacao(
            int totalLidos,
            int novos,
            int existentes) {
    }
}
