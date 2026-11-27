package br.com.nonetpsp.backend.config.security;

import br.com.nonetpsp.backend.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContaAtivaFilterTest {

    private UsuarioRepository usuarioRepository;
    private ContaAtivaFilter filtro;
    private FilterChain cadeia;
    private MockHttpServletRequest requisicao;
    private MockHttpServletResponse resposta;
    private MockHttpSession sessao;

    @BeforeEach
    void preparar() {

        usuarioRepository = mock(UsuarioRepository.class);
        filtro = new ContaAtivaFilter(usuarioRepository);
        cadeia = mock(FilterChain.class);

        sessao = new MockHttpSession();
        requisicao = new MockHttpServletRequest("GET", "/api/campanhas");
        requisicao.setSession(sessao);
        resposta = new MockHttpServletResponse();

        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(
                        "7", null, List.of()));
    }

    @AfterEach
    void limpar() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveSeguirQuandoContaEstaAtiva() throws Exception {

        when(usuarioRepository.existsByIdAndAtivoTrue(7L)).thenReturn(true);

        filtro.doFilter(requisicao, resposta, cadeia);

        verify(cadeia).doFilter(requisicao, resposta);
        assertFalse(sessao.isInvalid());
    }

    @Test
    void deveEncerrarSessaoQuandoContaFoiDesativada() throws Exception {

        when(usuarioRepository.existsByIdAndAtivoTrue(7L)).thenReturn(false);

        filtro.doFilter(requisicao, resposta, cadeia);

        verify(cadeia, never()).doFilter(any(), any());
        assertTrue(sessao.isInvalid());
        assertEquals(401, resposta.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveSeguirSemConsultarQuandoNaoHaSessao() throws Exception {

        SecurityContextHolder.clearContext();

        filtro.doFilter(requisicao, resposta, cadeia);

        verify(cadeia).doFilter(requisicao, resposta);
        verifyNoInteractions(usuarioRepository);
    }
}
