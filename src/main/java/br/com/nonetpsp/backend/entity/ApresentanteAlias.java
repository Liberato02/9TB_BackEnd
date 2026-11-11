
package br.com.nonetpsp.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "apresentante_aliases")
public class ApresentanteAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nomeAlternativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apresentante_id", nullable = false)
    private Apresentante apresentante;

    public ApresentanteAlias() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeAlternativo() {
        return nomeAlternativo;
    }

    public void setNomeAlternativo(String nomeAlternativo) {
        this.nomeAlternativo = nomeAlternativo;
    }

    public Apresentante getApresentante() {
        return apresentante;
    }

    public void setApresentante(Apresentante apresentante) {
        this.apresentante = apresentante;
    }
}
