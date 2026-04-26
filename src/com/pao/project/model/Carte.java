package com.pao.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Carte {
    private String titlu;
    private int anLansare;
    private Autor autor;
    private Categorie categorie;
    int nrPagini;
    private List<Exemplar> exemplare;

    public Carte(String titlu, int anLansare, Autor autor, Categorie categorie, int nrPagini) {
        this.titlu = titlu;
        this.anLansare = anLansare;
        this.autor = autor;
        this.categorie = categorie;
        this.nrPagini = nrPagini;
        exemplare = new ArrayList<>();
    }

    public String getTitlu() {
        return this.titlu;
    }
    public Autor getAutor() {
        return this.autor;
    }
    public Categorie getCategorie() {
        return this.categorie;
    }
    public List<Exemplar> getExemplare() {
        return exemplare;
    }
    public void adaugaExemplar(Exemplar e) {
        exemplare.add(e);
    }

    @Override
    public String toString() {
        return "Carte: titlu='" + titlu + "', an=" + anLansare + ", autor='"
                + autor.getNume() + "', categorie='" + categorie.getNume()
                + "', numar pagini=" + nrPagini;
    }
}
