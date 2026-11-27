package br.com.nonetpsp.backend.config.security;

import br.com.nonetpsp.backend.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ContaAtivaFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;

    public ContaAtivaFilter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Authentication autenticacao =
                SecurityContextHolder.getContext().getAuthentication();

        if (autenticacao != null
                && autenticacao.isAuthenticated()
                && !(autenticacao instanceof AnonymousAuthenticationToken)
                && !contaAtiva(autenticacao.getName())) {

            HttpSession sessao = request.getSession(false);

            if (sessao != null) {
                sessao.invalidate();
            }

            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean contaAtiva(String identificador) {
        try {
            return usuarioRepository.existsByIdAndAtivoTrue(
                    Long.valueOf(identificador));
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
