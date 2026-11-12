
package br.com.nonetpsp.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "campanha_titulos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_campanha_titulo", columnNames = { "campanha_id", "titulo_id" })
})
public class CampanhaTitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campanha_id", nullable = false)
    private Campanha campanha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "titulo_id", nullable = false)
    private Titulo titulo;

    public CampanhaTitulo() {
    }

    public Long getId() {
        return id;
    }

    public Campanha getCampanha() {
        return campanha;
    }

    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }
}
