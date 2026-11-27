CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,
    email VARCHAR(254),
    nome_usuario VARCHAR(50),

    senha_hash VARCHAR(100) NOT NULL,
    nivel_acesso VARCHAR(30) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    data_criacao TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ck_usuarios_identificador
        CHECK (email IS NOT NULL OR nome_usuario IS NOT NULL),

    CONSTRAINT ck_usuarios_nome_usuario_sem_arroba
        CHECK (nome_usuario NOT LIKE '%@%'),

    CONSTRAINT ck_usuarios_nivel_acesso
        CHECK (nivel_acesso IN ('SUPERADMINISTRADOR', 'ADMINISTRADOR', 'FUNCIONARIO'))
);

CREATE UNIQUE INDEX uk_usuarios_email
    ON usuarios (LOWER(email));

CREATE UNIQUE INDEX uk_usuarios_nome_usuario
    ON usuarios (LOWER(nome_usuario));
