package com.pao.project.model;

import java.util.Objects;

public class Cititor {
    private String nume;
    private String email;

    public Cititor(String nume, String email) {
        this.nume = nume;
        this.email = email;
    }

    public String getNume() {
        return this.nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Cititor: nume='" + nume + "', email='" + email + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cititor carte = (Cititor) o;
        return Objects.equals(nume, carte.getNume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume);
    }
}
