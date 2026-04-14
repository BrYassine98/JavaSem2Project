CREATE TABLE IF NOT EXISTS genre (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS pays (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    annee INT,
    genre_id INT NOT NULL REFERENCES genre(id),
    pays_id INT NOT NULL REFERENCES pays(id)
);

CREATE TABLE IF NOT EXISTS serie (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    saisons INT DEFAULT 1,
    genre_id INT NOT NULL REFERENCES genre(id),
    pays_id INT NOT NULL REFERENCES pays(id)
);

CREATE TABLE IF NOT EXISTS acteur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    pays_id INT REFERENCES pays(id)
);

CREATE TABLE IF NOT EXISTS film_acteur (
    film_id INT NOT NULL REFERENCES film(id) ON DELETE CASCADE,
    acteur_id INT NOT NULL REFERENCES acteur(id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, acteur_id)
);

CREATE TABLE IF NOT EXISTS serie_acteur (
    serie_id INT NOT NULL REFERENCES serie(id) ON DELETE CASCADE,
    acteur_id INT NOT NULL REFERENCES acteur(id) ON DELETE CASCADE,
    PRIMARY KEY (serie_id, acteur_id)
);
