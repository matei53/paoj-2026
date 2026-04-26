package com.pao.project.service;

import com.pao.project.exception.*;
import com.pao.project.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CarteService {
    private Map<ISBN, Carte> carti;
    private List<Imprumut> imprumuturi;

    private static int codISBN = 0;
    private static int codExemplar = 0;

    Scanner scanner = new Scanner(System.in);

    private CarteService() {
        this.carti = new HashMap<>();
        this.imprumuturi = new ArrayList<>();
    }

    private static class Holder {
        private static final CarteService INSTANCE = new CarteService();
    }

    public static CarteService getInstance() {
        return CarteService.Holder.INSTANCE;
    }

    public void adaugaCarte(Carte c, int nrExemplare) {
        if (nrExemplare <= 0)
            throw new NumarExemplareInvalid();
        while (nrExemplare != 0) {
            c.adaugaExemplar(new Exemplar(++codExemplar));
            nrExemplare --;
        }
        carti.put(new ISBN(++codISBN), c);
    }

    public void stergeCarte(ISBN cod) {
        if (!carti.containsKey(cod))
            throw new CarteNegasitaException();
        carti.remove(cod);
    }

    public Carte cautaDupaTitlu(String titlu) {
        for (Map.Entry<ISBN, Carte> c : carti.entrySet()) {
            if (c.getValue().getTitlu().equals(titlu))
                return c.getValue();
        }
        throw new CarteNegasitaException();
    }

    public List<Carte> cautaDupaAutor(Autor autor) {
        List<Carte> cartiGasite = new ArrayList<>();
        for (Map.Entry<ISBN, Carte> c : carti.entrySet()) {
            if (c.getValue().getAutor().equals(autor))
                cartiGasite.add(c.getValue());
        }
        return cartiGasite;
    }

    public List<Carte> cautaDupaCategorie(Categorie categorie) {
        List<Carte> cartiGasite = new ArrayList<>();
        for (Map.Entry<ISBN, Carte> c : carti.entrySet()) {
            if (c.getValue().getCategorie().equals(categorie))
                cartiGasite.add(c.getValue());
        }
        return cartiGasite;
    }

    public void adaugaExemplar(ISBN cod) {
        carti.get(cod).adaugaExemplar(new Exemplar(++codExemplar));
    }

    public void afiseazaCarti() {
        for (Map.Entry<ISBN, Carte> c : carti.entrySet()) {
            System.out.println(c.getValue().toString());
        }
    }

    public List<Exemplar> carteDisponibila(ISBN cod) {
        Carte c = carti.get(cod);
        if (c == null) throw new CarteNegasitaException();
        Set<Exemplar> exemplareImprumutate = new HashSet<>();
        for (Imprumut i : imprumuturi) {
            if (i.getCarte() == c && i.getDataReturnare() == null) {
                exemplareImprumutate.add(i.getExemplar());
            }
        }

        List<Exemplar> exemplareNeimprumutate = new ArrayList<>();
        for (Exemplar e : c.getExemplare()) {
            if (!exemplareImprumutate.contains(e)) {
                exemplareNeimprumutate.add(e);
            }
        }

        if (exemplareNeimprumutate.isEmpty())
            throw new CarteIndisponibilaException();
        return exemplareNeimprumutate;
    }

    public void adaugaImprumut(ISBN cod, Cititor cititor, LocalDate dataImprumut, LocalDate dataScadenta) {
        Carte c = carti.get(cod);
        if (c == null) throw new CarteNegasitaException();
        Exemplar e = carteDisponibila(cod).getFirst();
        imprumuturi.add(new Imprumut(cititor, c, e, dataImprumut, dataScadenta));
    }

    public void returneazaImprumut(Exemplar e) {
        for (Imprumut i : imprumuturi) {
            if (i.getExemplar().cod() == e.cod() && i.getDataReturnare() == null) {
                i.setDataReturnare();
                System.out.print("Adauga penalizare pentru deteriorare? Y/N ");
                String s = scanner.next();

                if (s.equals("Y") || s.equals("y")) {
                    System.out.print("Nivelul de deteriorare (1-10): ");
                    double n = scanner.nextDouble();
                    i.adaugaPenalizare(new PenalizareDeteriorare(n));
                }

                return;
            }
        }
        throw new ExemplarNeimprumutatException(e);
    }

    public void afiseazaIstoricImprumuturiCititor(Cititor c) {
        List<Imprumut> istoric = new ArrayList<>();
        for (Imprumut i : imprumuturi) {
            if (i.getCititor().equals(c)) {
                istoric.add(i);
            }
        }
        if (istoric.isEmpty())
            throw new CititorNegasitException(c.getNume());
        Collections.sort(istoric);
        for (Imprumut i : istoric) {
            System.out.println(i);
        }
    }

    public void topCartiImprumutate() {
        List<Map.Entry<Carte, Long>> top5 = imprumuturi.stream()
                .collect(Collectors.groupingBy(Imprumut::getCarte, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Carte, Long>comparingByValue().reversed())
                .limit(5)
                .toList();
        for (Map.Entry<Carte, Long> c : top5) {
            System.out.println(c.getKey().getTitlu() + " - " + c.getValue() + " imprumuturi");
        }
    }
}
