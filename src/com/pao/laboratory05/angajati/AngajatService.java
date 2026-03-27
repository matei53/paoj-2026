package com.pao.laboratory05.angajati;

import java.util.Arrays;
import java.util.Comparator;

public class AngajatService {
    private Angajat[] angajati;

    private AngajatService() {
        this.angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return AngajatService.Holder.INSTANCE;
    }

    void addAngajat(Angajat a) {
        Angajat[] added = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, added, 0, angajati.length);
        added[added.length - 1] = a;
        angajati = added;
    }

    void printAll() {
        for (Angajat a : angajati)
            System.out.println(a);
    }

    void listBySalary() {
        Angajat[] copy = angajati;
        Arrays.sort(copy);
        for (Angajat a : copy){
            System.out.println(a);
        }
    }

    void findByDepartament(String numeDept) {
        int ok = 0;
        for (Angajat a : angajati){
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept))
                System.out.println(a);
            ok = 1;
        }
        if (ok == 0)
            System.out.println("Niciun angajat în departamentul: " + numeDept);
    }
}
