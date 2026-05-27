package com.pao.project.model;

import java.util.Objects;

public class Categorie {
    private long id;
    private String nume;

    public Categorie(String nume) {
        this.nume = nume;
    }

    public Categorie() {}

    public long getId() {
        return id;
    }

    public String getNume() {
        return this.nume;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorie categorie = (Categorie) o;
        return Objects.equals(nume, categorie.getNume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume);
    }
}
