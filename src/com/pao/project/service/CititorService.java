package com.pao.project.service;

import com.pao.project.exception.CititorDejaExistentException;
import com.pao.project.exception.CititorNegasitException;
import com.pao.project.model.Cititor;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CititorService {
    private Set<Cititor> cititori;
    Scanner scanner = new Scanner(System.in);

    private CititorService() {
        this.cititori = new HashSet<>();
    }

    private static class Holder {
        private static final CititorService INSTANCE = new CititorService();
    }

    public static CititorService getInstance() {
        return Holder.INSTANCE;
    }

    public void adaugaCititor(Cititor c) {
        if (cititori.contains(c)) {
            throw new CititorDejaExistentException(c.getNume());
        }
        cititori.add(c);
    }

    public void stergeCititor(Cititor c) {
        cititori.remove(c);
    }

    public void modificaNumeCititor(Cititor c, String nume) {
        if (!cititori.contains(c)) {
            throw new CititorNegasitException(c.getNume());
        }
        if (cititori.contains(new Cititor(nume, null))) {
            throw new CititorDejaExistentException(nume);
        }

        cititori.remove(c);
        c.setNume(nume);
        cititori.add(c);
    }

    public void modificaEmailCititor(Cititor c, String email) {
        if (!cititori.contains(c)) {
            throw new CititorNegasitException(c.getNume());
        }
        cititori.remove(c);
        c.setEmail(email);
        cititori.add(c);
    }

    public Cititor cautaDupaNume(String nume) {
        for (Cititor c : cititori) {
            if (c.getNume().equals(nume)) {
                return c;
            }
        }
        throw new CititorNegasitException(nume);
    }

    public void afiseazaCititori() {
        for (Cititor c : cititori) {
            System.out.println(c.toString());
        }
    }
}
