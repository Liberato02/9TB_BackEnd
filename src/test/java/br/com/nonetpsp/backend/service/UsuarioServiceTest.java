package br.com.nonetpsp.backend.service;

import br.com.nonetpsp.backend.entity.NivelAcesso;
import br.com.nonetpsp.backend.entity.Usuario;
import br.com.nonetpsp.backend.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    @BeforeEach
    void preparar() {

        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        usuarioService = new UsuarioService(
                usuarioRepository,
                passwordEncoder);

        when(passwordEncoder.encode("senha-bem-longa")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    @Test
    void deveCriarPrimeiroSuperadministrador() {

        Usuario usuario = usuarioService.criarPrimeiroSuperadministrador(
                " Ana Souza ",
                " Ana@Exemplo.com.br ",
                "senha-bem-longa");

        assertEquals("Ana Souza", usuario.getNome());
        assertEquals("ana@exemplo.com.br", usuario.getEmail());
        assertEquals("hash", usuario.getSenhaHash());
        assertEquals(NivelAcesso.SUPERADMINISTRADOR, usuario.getNivelAcesso());
        assertTrue(usuario.isAtivo());
    }

    @Test
    void deveRecusarQuandoJaExisteSuperadministrador() {

        when(usuarioRepository.existsByNivelAcesso(NivelAcesso.SUPERADMINISTRADOR))
                .thenReturn(true);

        assertThrows(IllegalStateException.class, () -> usuarioService
                .criarPrimeiroSuperadministrador(
                        "Ana Souza",
                        "ana@exemplo.com.br",
                        "senha-bem-longa"));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveRecusarSenhaCurta() {

        assertThrows(IllegalArgumentException.class, () -> usuarioService
                .criarPrimeiroSuperadministrador(
                        "Ana Souza",
                        "ana@exemplo.com.br",
                        "curta"));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveRecusarEmailInvalido() {

        assertThrows(IllegalArgumentException.class, () -> usuarioService
                .criarPrimeiroSuperadministrador(
                        "Ana Souza",
                        "ana.exemplo.com.br",
                        "senha-bem-longa"));
    }
}
