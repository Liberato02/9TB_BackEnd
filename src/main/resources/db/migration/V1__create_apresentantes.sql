
CREATE TABLE apresentantes (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_apresentantes_nome
        UNIQUE (nome)
);
