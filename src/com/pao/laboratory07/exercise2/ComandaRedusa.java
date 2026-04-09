package com.pao.laboratory07.exercise2;

public final class ComandaRedusa extends Comanda {
    private double pret;
    protected int discountProcent;

    public ComandaRedusa(String nume, double pret, int discountProcent) {
        this.nume = nume;
        this.pret = pret;
        this.discountProcent = discountProcent;
    }

    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }
}
