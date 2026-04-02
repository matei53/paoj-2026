package com.pao.laboratory06.exercise2;

import java.util.Objects;
import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica{
    private String bonus;

    public double calculeazaVenitNetAnual(){
        if (areBonus())
            return 1.1 * venitBrutLunar * 12 * 0.55;
        return venitBrutLunar * 12 * 0.55;
    }

    @Override
    public boolean areBonus(){
        return Objects.equals(bonus, "DA");
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.bonus = in.next();
    }

    @Override
    public String tipContract() {
        return TipColaborator.CIM.name();
    }
}
