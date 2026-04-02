package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica{
    private double cheltuieliLunare;

    public double calculeazaVenitNetAnual(){
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;

        double impozit = venitNet * 0.1;

        double SALARIU_MINIM_BRUT_ANUAL = 4050 * 12;
        double cass;
        if (venitNet < 6 * SALARIU_MINIM_BRUT_ANUAL)
            cass = 0.1 * 6 * SALARIU_MINIM_BRUT_ANUAL;
        else if (venitNet <= 72 * SALARIU_MINIM_BRUT_ANUAL)
            cass = 0.1 * venitNet;
        else
            cass = 0.1 * 72 * SALARIU_MINIM_BRUT_ANUAL;

        double cas;
        if (venitNet < 12 * SALARIU_MINIM_BRUT_ANUAL)
            cas = 0;
        else if (venitNet <= 24 * SALARIU_MINIM_BRUT_ANUAL)
            cas = 0.25 * 12 * SALARIU_MINIM_BRUT_ANUAL;
        else
            cas = 0.25 * 24 * SALARIU_MINIM_BRUT_ANUAL;

        return venitNet - impozit - cass - cas;
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
        return TipColaborator.PFA.name();
    }
}
