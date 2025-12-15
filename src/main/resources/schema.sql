
-- Criação das tabelas para PostgreSQL

CREATE TABLE IF NOT EXISTS despesa (
    id BIGSERIAL PRIMARY KEY,
    numero_protocolo VARCHAR(20) NOT NULL UNIQUE,
    tipo_despesa VARCHAR(32) CHECK (tipo_despesa IN ('Obra de Edificação', 'Obra de Rodovias', 'Outros')),
    data_protocolo TIMESTAMP NOT NULL,
    data_vencimento DATE NOT NULL,
    credor VARCHAR(255) NOT NULL,
    descricao VARCHAR(1024) NOT NULL,
    valor NUMERIC(15,2) NOT NULL,
    status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS empenho (
    id BIGSERIAL PRIMARY KEY,
    numero_empenho VARCHAR(12) NOT NULL UNIQUE,
    data_empenho DATE NOT NULL,
    valor NUMERIC(15,2) NOT NULL,
    observacao VARCHAR(1024),
    despesa_id BIGINT NOT NULL,
    CONSTRAINT fk_despesa FOREIGN KEY (id) REFERENCES despesa(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS pagamento (
    id BIGSERIAL PRIMARY KEY,
    numero_pagamento VARCHAR(12) NOT NULL UNIQUE,
    data_pagamento DATE NOT NULL,
    valor NUMERIC(15,2) NOT NULL,
    observacao VARCHAR(1024),
    empenho_id BIGINT NOT NULL,
    CONSTRAINT fk_empenho FOREIGN KEY (id) REFERENCES empenho(id) ON DELETE RESTRICT
);

-- Índices auxiliares
CREATE INDEX IF NOT EXISTS idx_empenho_despesa ON empenho(despesa_id);
CREATE INDEX IF NOT EXISTS idx_pagamento_empenho ON pagamento(empenho_id);
