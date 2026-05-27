package com.pao.project.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PenalizareIntarziere extends Penalizare{
    private int zileIntarziere;

    public PenalizareIntarziere(LocalDate data1, LocalDate data2) {
        zileIntarziere = (int) ChronoUnit.DAYS.between(data1, data2);
    }

    public PenalizareIntarziere(long id, int zileIntarziere) {
        this.id = id;
        this.zileIntarziere = zileIntarziere;
    }

    public int getZileIntarziere() {
        return zileIntarziere;
    }

    public void setZileIntarziere(int zileIntarziere) {
        this.zileIntarziere = zileIntarziere;
    }

    public String getTip() {
        return "intarziere";
    }

    public double calculeazaSuma() {
        return zileIntarziere * 1.5;
    }
}
