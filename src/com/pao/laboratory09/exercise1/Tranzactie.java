package com.pao.laboratory09.exercise1;

import java.io.Serializable;

public class Tranzactie implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private double suma;
    private String data;
    private String contSursa;
    private String contDestinatie;
    private TipTranzactie tip;
    private transient String note;

    public Tranzactie(int id, double suma, String data, String contSursa, String contDestinatie, TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.contSursa = contSursa;
        this.contDestinatie = contDestinatie;
        this.tip = tip;
        this.note = "procesat";
    }

    public int getId() {
        return this.id;
    }

    public String getDataYYYYMM() {
        return this.data.substring(0, 7);
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "[%d] %s %s: %.2f RON | %s -> %s".formatted(id, data, tip.toString(), suma, contSursa, contDestinatie);
    }
}
