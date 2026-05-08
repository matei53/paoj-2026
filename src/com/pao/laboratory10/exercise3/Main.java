package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = new ArrayList<>(List.of(
            new Tranzactie(1,  1500.00, "2024-01-05", TipTranzactie.CREDIT, "CONT_A"),
            new Tranzactie(2,   200.00, "2024-01-10", TipTranzactie.DEBIT,  "CONT_B"),
            new Tranzactie(3,   300.00, "2024-01-20", TipTranzactie.CREDIT, "CONT_A"),
            new Tranzactie(4,   750.00, "2024-02-03", TipTranzactie.CREDIT, "CONT_C"),
            new Tranzactie(5,   100.00, "2024-02-14", TipTranzactie.DEBIT,  "CONT_A"),
            new Tranzactie(6,  2000.00, "2024-02-28", TipTranzactie.CREDIT, "CONT_B"),
            new Tranzactie(7,   450.00, "2024-03-01", TipTranzactie.DEBIT,  "CONT_C"),
            new Tranzactie(8,   600.00, "2024-03-15", TipTranzactie.CREDIT, "CONT_A"),
            new Tranzactie(9,   350.00, "2024-03-22", TipTranzactie.DEBIT,  "CONT_B"),
            new Tranzactie(10,  900.00, "2024-03-30", TipTranzactie.CREDIT, "CONT_C")
        ));

        System.out.println("=== 1. Lista tuturor tranzactiilor CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\n=== 2. Suma totala procesata ===");
        System.out.printf("Total procesat: %.2f RON\n",
                tranzactii.stream().mapToDouble(Tranzactie::getSuma).sum());

        System.out.println("\n=== 3. Suma procesata pe luna ===");
        TreeMap<String, Double> sumaPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(t -> t.getData().substring(0, 7),
                        TreeMap::new, Collectors.summingDouble(Tranzactie::getSuma)));
        for (var entry : sumaPerLuna.entrySet()) {
            System.out.printf("%s: %.2f RON\n", entry.getKey(), entry.getValue());
        }

        System.out.println("\n=== 4. Top 3 tranzactii dupa suma ===");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream().sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3).forEach(System.out::println);;

        System.out.println("\n=== 5. Conturi sursa unice ===");
        System.out.print("Conturi sursa unice: ");
        System.out.println(tranzactii.stream()
                .map(Tranzactie::getContSursa).distinct().collect(Collectors.toList()));

        System.out.println("\n=== 6. Suma procesata medie ===");
        System.out.printf("Suma medie: %.2f RON\n",
                tranzactii.stream().mapToDouble(Tranzactie::getSuma).average().orElse(0.0));

        System.out.println("\n=== 7. Extras pe luna ===");
        TreeMap<String, List<Tranzactie>> tranzactiiPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(t -> t.getData().substring(0, 7),
                        TreeMap::new, Collectors.toList()));
        for (var entry : tranzactiiPerLuna.entrySet()) {
            int nrTranzactii = entry.getValue().size();
            double totalLuna = entry.getValue().stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON\n",
                    entry.getKey(), nrTranzactii, totalLuna);
        }
    }
}
