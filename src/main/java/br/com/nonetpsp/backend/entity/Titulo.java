
package br.com.nonetpsp.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "titulos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_titulos_protocolo_data", columnNames = {
                "numero_protocolo",
                "data_protocolo"
        })
})
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_protocolo", nullable = false)
    private String numeroProtocolo;

    @Column(name = "data_protocolo", nullable = false)
    private LocalDate dataProtocolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apresentante_id", nullable = false)
    private Apresentante apresentante;

    @Column(name = "valor_custas", precision = 15, scale = 2)
    private BigDecimal valorCustas;

    @Column(name = "valor_titulo", precision = 15, scale = 2)
    private BigDecimal valorTitulo;

    public Titulo() {
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

    public Apresentante getApresentante() {
        return apresentante;
    }

    public void setApresentante(Apresentante apresentante) {
        this.apresentante = apresentante;
    }

    public BigDecimal getValorCustas() {
        return valorCustas;
    }

    public void setValorCustas(BigDecimal valorCustas) {
        this.valorCustas = valorCustas;
    }

    public BigDecimal getValorTitulo() {
        return valorTitulo;
    }

    public void setValorTitulo(BigDecimal valorTitulo) {
        this.valorTitulo = valorTitulo;
    }
}
