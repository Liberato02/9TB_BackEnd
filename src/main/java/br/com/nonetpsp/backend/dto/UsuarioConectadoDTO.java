package br.com.nonetpsp.backend.dto;

import br.com.nonetpsp.backend.entity.NivelAcesso;
import br.com.nonetpsp.backend.entity.Usuario;

public record UsuarioConectadoDTO(
        Long id,
        String nome,
        String email,
        String nomeUsuario,
        NivelAcesso nivelAcesso) {

    public static UsuarioConectadoDTO de(Usuario usuario) {
        return new UsuarioConectadoDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getNomeUsuario(),
                usuario.getNivelAcesso());
    }
}
