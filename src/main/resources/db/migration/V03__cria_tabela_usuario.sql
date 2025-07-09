CREATE TABLE usuario (
    id_usuario BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL,
    token_redefinicao_senha VARCHAR(255),
    data_expiracao_token TIMESTAMP
);