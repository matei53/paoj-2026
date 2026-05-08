package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

public class Tranzactie {
    private int id;
    private double suma;
    private String data;
    private TipTranzactie tip;
    private String contSursa;

    public Tranzactie (int id, double suma, String data, TipTranzactie tip, String contSursa) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
        this.contSursa = contSursa;
    }

    public int getId() {
        return this.id;
    }
    public double getSuma() {
        return this.suma;
    }
    public String getData() {
        return this.data;
    }
    public TipTranzactie getTip() {
        return this.tip;
    }
    public String getContSursa() {
        return this.contSursa;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s %s: %.2f RON", id, data, tip.toString(), suma);
    }
}

