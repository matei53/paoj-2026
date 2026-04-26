package com.pao.project.model;

import com.pao.project.exception.NivelDeteriorareException;

public class PenalizareDeteriorare extends Penalizare{
    private double nivelDeteriorare;

    public PenalizareDeteriorare(double nivelDeteriorare) {
        if (nivelDeteriorare < 1 || nivelDeteriorare > 10)
            throw new NivelDeteriorareException();
        this.nivelDeteriorare = nivelDeteriorare;
    }

    public String getTip() {
        return "deteriorare";
    }

    public double calculeazaSuma() {
        return nivelDeteriorare * 10;
    }
}
