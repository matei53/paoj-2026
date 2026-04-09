package com.pao.laboratory07.exercise3;

public final class ComandaRedusa extends Comanda {
    private double pret;
    protected int discountProcent;

    public int getDiscountProcent() {
        return discountProcent;
    }

    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        this.nume = nume;
        this.pret = pret;
        this.discountProcent = discountProcent;
        this.client = client;
    }

    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }
    public String getType() {
        return "DISCOUNTED";
    }
}