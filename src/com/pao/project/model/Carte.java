package com.pao.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Carte {
    private long id;
    private String titlu;
    private Integer anLansare;
    private Autor autor;
    private Categorie categorie;
    int nrPagini;
    private List<Exemplar> exemplare;

    public Carte(String titlu, Integer anLansare, Autor autor, Categorie categorie, int nrPagini) {
        this.titlu = titlu;
        this.anLansare = anLansare;
        this.autor = autor;
        this.categorie = categorie;
        this.nrPagini = nrPagini;
        exemplare = new ArrayList<>();
    }

    public Carte() {
        exemplare = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getTitlu() {
        return this.titlu;
    }

    public Integer getAnLansare() {
        return anLansare;
    }

    public Autor getAutor() {
        return this.autor;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public int getNrPagini() {
        return nrPagini;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public void setAnLansare(Integer anLansare) {
        this.anLansare = anLansare;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setNrPagini(int nrPagini) {
        this.nrPagini = nrPagini;
    }

    public List<Exemplar> getExemplare() {
        return exemplare;
    }
    public void adaugaExemplar(Exemplar e) {
        exemplare.add(e);
    }

    @Override
    public String toString() {
        if (autor != null)
            return "Carte: id=" + id + ", titlu='" + titlu + "', an=" + anLansare + ", autor='"
                    + autor.getNume() + "', categorie='" + categorie.getNume()
                    + "', numar pagini=" + nrPagini;
        else
            return "Carte: id=" + id + ", titlu='" + titlu + "', an=" + anLansare
                    + ", categorie='" + categorie.getNume()
                    + "', numar pagini=" + nrPagini;
    }
}
