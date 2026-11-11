
CREATE TABLE apresentante_aliases (
    id BIGSERIAL PRIMARY KEY,

    nome_alternativo VARCHAR(150) NOT NULL,

    apresentante_id BIGINT NOT NULL,

    CONSTRAINT uk_apresentante_aliases_nome
        UNIQUE (nome_alternativo),

    CONSTRAINT fk_alias_apresentante
        FOREIGN KEY (apresentante_id)
        REFERENCES apresentantes(id)
);
