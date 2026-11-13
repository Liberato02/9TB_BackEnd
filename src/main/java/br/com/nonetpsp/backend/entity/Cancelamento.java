
package br.com.nonetpsp.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cancelamentos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cancelamentos_protocolo_data", columnNames = {
                "numero_protocolo",
                "data_protocolo"
        })
})
public class Cancelamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_protocolo", nullable = false, length = 20)
    private String numeroProtocolo;

    @Column(name = "data_protocolo", nullable = false)
    private LocalDate dataProtocolo;

    @Column(name = "data_cancelamento", nullable = false)
    private LocalDate dataCancelamento;

    @Column(name = "data_importacao", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dataImportacao;

    public Cancelamento() {
    }

    public Long getId() {
        return id;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public LocalDate getDataProtocolo() {
        return dataProtocolo;
    }

    public void setDataProtocolo(LocalDate dataProtocolo) {
        this.dataProtocolo = dataProtocolo;
    }

    public LocalDate getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDate dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public LocalDateTime getDataImportacao() {
        return dataImportacao;
    }
}
