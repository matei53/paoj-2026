package com.pao.laboratory05.audit;

public class Angajat implements Comparable<Angajat> {
    private String nume;
    private Departament departament;
    private double salariu;

    public Angajat(String nume, Departament departament, double salariu) {
        this.nume = nume;
        this.departament = departament;
        this.salariu = salariu;
    }

    public String getNume(){
        return this.nume;
    }
    public Departament getDepartament(){
        return this.departament;
    }
    public double getSalariu(){
        return this.salariu;
    }

    public String toString(){
        return "Angajat{nume='" + nume + "', departament=Departament[nume=" +
                departament.nume() + ", locatie=" + departament.locatie() + "], salariu=" + salariu + "}";
    }

    public int compareTo(Angajat other){
        return (-1) * Double.compare(this.salariu, other.getSalariu());
    }
}
