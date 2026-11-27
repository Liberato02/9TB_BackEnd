package br.com.nonetpsp.backend.controller;

import br.com.nonetpsp.backend.dto.UsuarioConectadoDTO;
import br.com.nonetpsp.backend.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticacaoController {

    private final UsuarioService usuarioService;

    public AutenticacaoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/api/auth/eu")
    public ResponseEntity<UsuarioConectadoDTO> eu(Authentication authentication) {

        return usuarioService.buscarAtivoPorId(Long.valueOf(authentication.getName()))
                .map(UsuarioConectadoDTO::de)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build());
    }
}
