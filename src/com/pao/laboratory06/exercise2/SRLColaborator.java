package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica{
    private double cheltuieliLunare;

    public double calculeazaVenitNetAnual(){
        return (venitBrutLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return TipColaborator.SRL.name();
    }
}
