CREATE TABLE entrenador (
    id_entrenador BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre_entrenador VARCHAR(100) NOT NULL,
    pueblo_origen VARCHAR(100),
    fecha_registro TIMESTAMP,
    CONSTRAINT uk_entrenador_username UNIQUE (username)
);

CREATE TABLE pokemon_capturado (
    id_pokemon_capturado BIGSERIAL PRIMARY KEY,
    id_entrenador BIGINT NOT NULL,
    num_pokedex INTEGER NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    location VARCHAR(30) NOT NULL,
    fecha_captura TIMESTAMP NOT NULL,
    sprite_url VARCHAR(255),
    CONSTRAINT fk_pokemon_entrenador FOREIGN KEY (id_entrenador)
        REFERENCES entrenador (id_entrenador)
);

CREATE INDEX idx_pokemon_entrenador
    ON pokemon_capturado (id_entrenador);

CREATE INDEX idx_pokemon_num_pokedex
    ON pokemon_capturado (num_pokedex);
