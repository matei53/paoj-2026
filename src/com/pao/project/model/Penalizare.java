package com.pao.project.model;

public abstract class Penalizare {
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract String getTip();
    public abstract double calculeazaSuma();
}
