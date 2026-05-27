package com.pao.project.model;

import com.pao.project.exception.NivelDeteriorareException;

public class PenalizareDeteriorare extends Penalizare{
    private double nivelDeteriorare;

    public PenalizareDeteriorare(double nivelDeteriorare) {
        if (nivelDeteriorare < 1 || nivelDeteriorare > 10)
            throw new NivelDeteriorareException();
        this.nivelDeteriorare = nivelDeteriorare;
    }

    public PenalizareDeteriorare(long id, double nivelDeteriorare) {
        this.id = id;
        if (nivelDeteriorare < 1 || nivelDeteriorare > 10)
            throw new NivelDeteriorareException();
        this.nivelDeteriorare = nivelDeteriorare;
    }

    public double getNivelDeteriorare() {
        return nivelDeteriorare;
    }

    public void setNivelDeteriorare(double nivelDeteriorare) {
        this.nivelDeteriorare = nivelDeteriorare;
    }

    public String getTip() {
        return "deteriorare";
    }

    public double calculeazaSuma() {
        return nivelDeteriorare * 10;
    }
}
