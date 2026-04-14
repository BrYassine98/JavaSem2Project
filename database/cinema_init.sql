CREATE TABLE IF NOT EXISTS genre (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS pays (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS acteur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    pays_id INT REFERENCES pays(id)
);

CREATE TABLE IF NOT EXISTS film (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    annee INT,
    genre_id INT REFERENCES genre(id),
    pays_id INT REFERENCES pays(id)
);

CREATE TABLE IF NOT EXISTS serie (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    saisons INT DEFAULT 1,
    genre_id INT REFERENCES genre(id),
    pays_id INT REFERENCES pays(id)
);
