package com.pao.laboratory07.exercise3;

public final class ComandaStandard extends Comanda {
    private double pret;

    public ComandaStandard(String nume, double pret, String client) {
        this.nume = nume;
        this.pret = pret;
        this.client = client;
    }

    public double pretFinal() {
        return pret;
    }
    public String getType() {
        return "STANDARD";
    }
}