DROP TABLE IF EXISTS penalizare_deteriorare;
DROP TABLE IF EXISTS penalizare_intarziere;
DROP TABLE IF EXISTS imprumut;
DROP TABLE IF EXISTS exemplar;
DROP TABLE IF EXISTS carte;
DROP TABLE IF EXISTS cititor;
DROP TABLE IF EXISTS categorie;
DROP TABLE IF EXISTS autor;

CREATE TABLE autor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nume TEXT NOT NULL UNIQUE
);

CREATE TABLE categorie (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nume TEXT NOT NULL UNIQUE
);

CREATE TABLE cititor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nume TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE
);

CREATE TABLE carte (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titlu TEXT NOT NULL,
    an_lansare INTEGER,
    id_autor INTEGER,
    id_categorie INTEGER NOT NULL,
    nr_pagini INTEGER NOT NULL,
    FOREIGN KEY (id_autor) REFERENCES autor(id)
                   ON DELETE SET NULL
                   ON UPDATE CASCADE,
    FOREIGN KEY (id_categorie) REFERENCES categorie(id)
                   ON DELETE CASCADE
                   ON UPDATE CASCADE,
    UNIQUE(titlu, id_autor)
);

CREATE TABLE exemplar (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cod INTEGER NOT NULL,
    id_carte INTEGER NOT NULL,
    FOREIGN KEY (id_carte) REFERENCES carte(id)
                      ON DELETE CASCADE
                      ON UPDATE CASCADE
);

CREATE TABLE imprumut (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_cititor INTEGER NOT NULL,
    id_exemplar INTEGER NOT NULL,
    data_imprumut TEXT NOT NULL,
    data_scadenta TEXT NOT NULL,
    data_returnare TEXT,
    FOREIGN KEY (id_cititor) REFERENCES cititor(id)
                      ON DELETE CASCADE
                      ON UPDATE CASCADE,
    FOREIGN KEY (id_exemplar) REFERENCES exemplar(id)
                      ON DELETE CASCADE
                      ON UPDATE CASCADE
);

CREATE TABLE penalizare_deteriorare (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_imprumut INTEGER NOT NULL UNIQUE,
    nivel_deteriorare REAL NOT NULL
                      CHECK ( nivel_deteriorare >= 1 and nivel_deteriorare <= 10 ),
    FOREIGN KEY (id_imprumut) REFERENCES imprumut(id)
                      ON DELETE CASCADE
                      ON UPDATE CASCADE
);

CREATE TABLE penalizare_intarziere (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_imprumut INTEGER NOT NULL UNIQUE,
    zile_intarziere INTEGER NOT NULL CHECK (zile_intarziere >= 1),
    FOREIGN KEY (id_imprumut) REFERENCES imprumut(id)
                      ON DELETE CASCADE
                      ON UPDATE CASCADE
);