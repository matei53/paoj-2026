package com.pao.project.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PenalizareIntarziere extends Penalizare{
    private int zileIntarziere;

    public PenalizareIntarziere(LocalDate data1, LocalDate data2) {
        zileIntarziere = (int) ChronoUnit.DAYS.between(data1, data2);
    }

    public String getTip() {
        return "intarziere";
    }

    public double calculeazaSuma() {
        return zileIntarziere * 1.5;
    }
}
