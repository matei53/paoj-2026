package com.pao.project.model;

import java.util.Objects;

public class Autor {
    private long id;
    private String nume;

    public Autor(String nume) {
        this.nume = nume;
    }

    public Autor() {}

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
    public String toString() {
        return "Autor{" + "id=" + id + ", nume=" + nume + '}';
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
