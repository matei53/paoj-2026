package com.pao.laboratory07.exercise3;

public final class ComandaGratuita extends Comanda {
    public ComandaGratuita(String nume, String client) {
        this.nume = nume;
        this.client = client;
    }

    public double pretFinal() {
        return 0.0;
    }
    public String getType() {
        return "GIFT";
    }
}
