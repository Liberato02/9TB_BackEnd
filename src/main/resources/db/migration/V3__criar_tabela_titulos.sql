
CREATE TABLE titulos (
    id BIGSERIAL PRIMARY KEY,

    numero_protocolo VARCHAR(255) NOT NULL,
    data_protocolo DATE NOT NULL,

    apresentante_id BIGINT NOT NULL,

    valor_custas NUMERIC(15, 2),
    valor_titulo NUMERIC(15, 2),

    CONSTRAINT uk_titulos_protocolo_data
        UNIQUE (numero_protocolo, data_protocolo),

    CONSTRAINT fk_titulos_apresentante
        FOREIGN KEY (apresentante_id)
        REFERENCES apresentantes(id)
);

CREATE INDEX idx_titulos_apresentante
    ON titulos(apresentante_id);
