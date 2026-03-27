package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti;

    private BibliotecaService() {
        this.carti = new Carte[0];
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte c) {
        Carte[] added = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, added, 0, carti.length);
        added[added.length - 1] = c;
        carti = added;
    }

    public void listSortedByRating() {
        Carte[] copy = carti;
        Arrays.sort(copy);
        for (Carte c : copy){
            System.out.println(c);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti;
        Arrays.sort(copy, comparator);
        for (Carte c : copy){
            System.out.println(c);
        }
    }
}
