package com.pao.project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Imprumut implements Comparable<Imprumut>{
    private Cititor cititor;
    private Carte carte;
    private Exemplar exemplar;
    private LocalDate dataImprumut;
    private LocalDate dataScadenta;
    private LocalDate dataReturnare;
    private List<Penalizare> penalizari;

    public Imprumut(Cititor cititor, Carte carte, Exemplar exemplar, LocalDate dataImprumut, LocalDate dataScadenta) {
        this.cititor = cititor;
        this.carte = carte;
        this.exemplar = exemplar;
        this.dataImprumut = dataImprumut;
        this.dataScadenta = dataScadenta;
        this.dataReturnare = null;
        this.penalizari = new ArrayList<>();
    }

    public void setDataReturnare() {
        dataReturnare = LocalDate.now();
        if (dataReturnare.isAfter(dataScadenta))
            adaugaPenalizare(new PenalizareIntarziere(dataScadenta, dataReturnare));
    }

    public void adaugaPenalizare(Penalizare p) {
        penalizari.add(p);
    }

    public Carte getCarte() {
        return carte;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public LocalDate getDataReturnare() {
        return dataReturnare;
    }

    public Cititor getCititor() {
        return cititor;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("IMPRUMUT:\n"
                + "   - cititor: " + cititor.getNume() + "\n"
                + "   - carte: " + carte.getTitlu() + "\n"
                + "   - exemplar: " + exemplar.cod() + "\n"
                + "   - data imprumut: " + dataImprumut.toString() + "\n"
                + "   - data scadenta: " + dataScadenta.toString() + "\n");
        if (dataReturnare != null) {
            s.append("   - data returnare: ").append(dataReturnare.toString()).append("\n");
            if (!penalizari.isEmpty()) {
                for (Penalizare p : penalizari) {
                    s.append("      - penalizare: ").append(p.getTip()).append(" - ")
                            .append(p.calculeazaSuma()).append("lei\n");
                }
                return s.toString();
            }
            else {
                return s.toString();
            }
        }
        else {
            return s.toString();
        }
    }

    @Override
    public int compareTo(Imprumut other) {
        return this.dataScadenta.compareTo(other.dataScadenta); // LocalDate already implements Comparable
    }
}
