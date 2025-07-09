CREATE TABLE relacionamento (
    id_usuario BIGINT NOT NULL,
    id_skills BIGINT NOT NULL,
    level INT NOT NULL CHECK (level >= 0 AND level <= 10),
    PRIMARY KEY (id_usuario, id_skills),
    CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_skills FOREIGN KEY (id_skills) REFERENCES skills(id_skill)
);