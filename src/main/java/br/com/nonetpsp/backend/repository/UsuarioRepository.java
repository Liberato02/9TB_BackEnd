package br.com.nonetpsp.backend.repository;

import br.com.nonetpsp.backend.entity.NivelAcesso;
import br.com.nonetpsp.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    @Query("""
            SELECT u FROM Usuario u
            WHERE LOWER(u.email) = LOWER(:identificador)
               OR LOWER(u.nomeUsuario) = LOWER(:identificador)
            """)
    Optional<Usuario> buscarPorIdentificador(
            @Param("identificador") String identificador);

    boolean existsByNivelAcesso(NivelAcesso nivelAcesso);

    boolean existsByEmailIgnoreCase(String email);
}
