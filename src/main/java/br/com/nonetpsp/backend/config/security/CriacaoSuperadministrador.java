package br.com.nonetpsp.backend.config.security;

import br.com.nonetpsp.backend.entity.Usuario;
import br.com.nonetpsp.backend.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CriacaoSuperadministrador implements ApplicationRunner {

    private static final Logger log =
            LoggerFactory.getLogger(CriacaoSuperadministrador.class);

    private final UsuarioService usuarioService;
    private final ApplicationContext contexto;

    public CriacaoSuperadministrador(
            UsuarioService usuarioService,
            ApplicationContext contexto) {

        this.usuarioService = usuarioService;
        this.contexto = contexto;
    }

    @Override
    public void run(ApplicationArguments args) {

        if (!args.containsOption("criar-superadministrador")) {
            return;
        }

        int codigo = criar();
        System.exit(SpringApplication.exit(contexto, () -> codigo));
    }

    private int criar() {
        try {
            Usuario usuario = usuarioService.criarPrimeiroSuperadministrador(
                    System.getenv("ARIA_ADMIN_NOME"),
                    System.getenv("ARIA_ADMIN_EMAIL"),
                    System.getenv("ARIA_ADMIN_SENHA"));

            log.info("Superadministrador criado: {}", usuario.getEmail());
            return 0;
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Superadministrador não criado: {}", e.getMessage());
            return 1;
        }
    }
}
