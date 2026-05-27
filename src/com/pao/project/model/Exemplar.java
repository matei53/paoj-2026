package com.pao.project.model;

public class Exemplar {
    private long id;
    private int cod;

    public Exemplar (long id, int cod) {
        this.id = id;
        this.cod = cod;
    }
    public Exemplar (int cod) {
        this.cod = cod;
    }

    public long getId() {
        return id;
    }

    public int getCod() {
        return cod;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return  "Exemplar{" + "id=" + id + ", cod=" + cod + '}';
    }
}
