package br.com.nonetpsp.backend.config.security;

import br.com.nonetpsp.backend.entity.Usuario;
import br.com.nonetpsp.backend.repository.UsuarioRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identificador)
            throws UsernameNotFoundException {

        String identificadorLimpo = identificador == null
                ? ""
                : identificador.trim();

        Usuario usuario = usuarioRepository
                .buscarPorIdentificador(identificadorLimpo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado."));

        return User.withUsername(String.valueOf(usuario.getId()))
                .password(usuario.getSenhaHash())
                .disabled(!usuario.isAtivo())
                .roles(usuario.getNivelAcesso().name())
                .build();
    }
}
