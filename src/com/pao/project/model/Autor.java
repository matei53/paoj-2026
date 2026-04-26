package com.pao.project.model;

import java.util.Objects;

public class Autor {
    private String nume;

    public Autor(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return this.nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(nume, autor.getNume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume);
    }
}
