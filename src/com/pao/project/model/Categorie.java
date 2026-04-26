package com.pao.project.model;

import java.util.Objects;

public class Categorie {
    private String nume;

    public Categorie(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return this.nume;
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
