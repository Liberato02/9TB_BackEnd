
CREATE TABLE campanhas (
    id BIGSERIAL PRIMARY KEY,

    nome VARCHAR(255) NOT NULL,

    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE campanha_titulos (
    id BIGSERIAL PRIMARY KEY,

    campanha_id BIGINT NOT NULL,
    titulo_id BIGINT NOT NULL,

    CONSTRAINT fk_campanha_titulos_campanha
        FOREIGN KEY (campanha_id)
        REFERENCES campanhas(id),

    CONSTRAINT fk_campanha_titulos_titulo
        FOREIGN KEY (titulo_id)
        REFERENCES titulos(id),

    CONSTRAINT uk_campanha_titulo
        UNIQUE (campanha_id, titulo_id)
);

CREATE INDEX idx_campanha_titulos_campanha
    ON campanha_titulos(campanha_id);

CREATE INDEX idx_campanha_titulos_titulo
    ON campanha_titulos(titulo_id);
