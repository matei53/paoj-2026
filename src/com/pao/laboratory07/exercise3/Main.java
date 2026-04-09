package com.pao.laboratory07.exercise3;

import java.util.*;

import com.pao.laboratory07.exercise1.OrderState;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();
        int nrStandard = 0, nrDiscounted = 0, nrGift = 0;
        double sumaStandard = 0, sumaDiscounted = 0;
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");
            if (tokens[0].equals("STANDARD")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                String client = tokens[3];
                Comanda c = new ComandaStandard(nume, pret, client);
                comenzi.add(c);
                nrStandard++;
                sumaStandard += c.pretFinal();
            } else if (tokens[0].equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                String client = tokens[4];
                Comanda c = new ComandaRedusa(nume, pret, discount, client);
                comenzi.add(c);
                nrDiscounted++;
                sumaDiscounted += c.pretFinal();
            } else if (tokens[0].equals("GIFT")) {
                String nume = tokens[1];
                String client = tokens[2];
                Comanda c = new ComandaGratuita(nume, client);
                comenzi.add(c);
                nrGift++;
            }
        }
        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        System.out.println();

        while (true) {
            String instruction = sc.nextLine().trim();
            String[] tokens = instruction.split(" ");
            switch (tokens[0]) {
                case "STATS" -> {
                    System.out.println("\n--- STATS ---");
                    Map<String, Double> averages = comenzi.stream().collect(groupingBy(Comanda::getType,
                            averagingDouble(Comanda::pretFinal)));
                    for (Map.Entry<String, Double> e : averages.entrySet()) {
                        System.out.println(e.getKey() + " : medie = " + String.format("%.2f", e.getValue()) + " lei");
                    }
                }
                case "FILTER" -> {
                    double filtru = Double.parseDouble(tokens[1]);
                    System.out.println("\n--- FILTER (>= " + String.format("%.2f", filtru) + ") ---");
                    List<Comanda> lista = comenzi.stream().filter(c -> c.pretFinal() >= filtru).toList();
                    for (Comanda c : lista) {
                        System.out.println(c.descriere());
                    }
                }
                case "SORT" -> {
                    System.out.println("\n--- SORT (by client, then by pret) ---");
                    List<Comanda> lista = comenzi;
                    lista.sort(Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal));
                    for (Comanda c : lista) {
                        System.out.println(c.descriere());
                    }
                }
                case "SPECIAL" -> {
                    System.out.println("\n--- SPECIAL (discount > 15%) ---");
                    List<Comanda> lista = comenzi.stream().filter(c ->
                            c instanceof ComandaRedusa && ((ComandaRedusa) c).getDiscountProcent() >= 15).toList();
                    for (Comanda c : lista) {
                        System.out.println(c.descriere());
                    }
                }
                case "QUIT" -> {
                    return;
                }
            }
        }
    }
}
