package com.pao.laboratory07.exercise2;

public final class ComandaStandard extends Comanda {
    private double pret;

    public ComandaStandard(String nume, double pret) {
        this.nume = nume;
        this.pret = pret;
    }

    public double pretFinal() {
        return pret;
    }
}
