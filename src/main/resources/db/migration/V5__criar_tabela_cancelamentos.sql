
CREATE TABLE cancelamentos (
    id BIGSERIAL PRIMARY KEY,

    numero_protocolo VARCHAR(20) NOT NULL,
    data_protocolo DATE NOT NULL,
    data_cancelamento DATE NOT NULL,

    data_importacao TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_cancelamentos_protocolo_data
        UNIQUE (numero_protocolo, data_protocolo)
);

CREATE INDEX idx_cancelamentos_data_cancelamento
    ON cancelamentos(data_cancelamento);
