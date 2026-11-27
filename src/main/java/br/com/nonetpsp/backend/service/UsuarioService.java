package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.NivelAcesso;
import br.com.nonetpsp.backend.entity.Usuario;
import br.com.nonetpsp.backend.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final int TAMANHO_MINIMO_SENHA = 12;
    private static final int TAMANHO_MAXIMO_SENHA = 72;
    private static final int TAMANHO_MAXIMO_NOME = 150;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarAtivoPorId(Long id) {
        return usuarioRepository.findById(id)
                .filter(Usuario::isAtivo);
    }

    @Transactional
    public Usuario criarPrimeiroSuperadministrador(
            String nome,
            String email,
            String senha) {

        String nomeLimpo = nome == null ? "" : nome.trim();
        String emailLimpo = email == null
                ? ""
                : email.trim().toLowerCase(Locale.ROOT);

        if (nomeLimpo.isEmpty() || nomeLimpo.length() > TAMANHO_MAXIMO_NOME) {
            throw new IllegalArgumentException(
                    "Informe o nome com até 150 caracteres.");
        }

        if (!emailLimpo.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException(
                    "Informe um e-mail válido.");
        }

        if (senha == null
                || senha.length() < TAMANHO_MINIMO_SENHA
                || senha.getBytes(StandardCharsets.UTF_8).length > TAMANHO_MAXIMO_SENHA) {
            throw new IllegalArgumentException(
                    "A senha precisa ter de 12 a 72 caracteres.");
        }

        if (usuarioRepository.existsByNivelAcesso(NivelAcesso.SUPERADMINISTRADOR)) {
            throw new IllegalStateException(
                    "Já existe um superadministrador.");
        }

        if (usuarioRepository.existsByEmailIgnoreCase(emailLimpo)) {
            throw new IllegalStateException(
                    "Já existe um usuário com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nomeLimpo);
        usuario.setEmail(emailLimpo);
        usuario.setSenhaHash(passwordEncoder.encode(senha));
        usuario.setNivelAcesso(NivelAcesso.SUPERADMINISTRADOR);
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }
}
