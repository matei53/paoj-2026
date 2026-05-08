package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON

        LinkedList<Tranzactie> tranzactii = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String[] parti = scanner.nextLine().split(" ");
            String comanda = parti[0];

            switch (comanda) {
                case "ENQUEUE": {
                    int id = Integer.parseInt(parti[1]);
                    double suma = Double.parseDouble(parti[2]);
                    String data = parti[3];
                    TipTranzactie tip = TipTranzactie.valueOf(parti[4]);
                    tranzactii.addLast(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "DEQUEUE": {
                    if (tranzactii.isEmpty())
                        System.out.println("Coada goala.");
                    else {
                        Tranzactie t = tranzactii.removeFirst();
                        System.out.println("Procesat: " + t.toString());
                    }
                    break;
                }
                case "PUSH": {
                    int id = Integer.parseInt(parti[1]);
                    double suma = Double.parseDouble(parti[2]);
                    String data = parti[3];
                    TipTranzactie tip = TipTranzactie.valueOf(parti[4]);
                    tranzactii.addFirst(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "POP": {
                    if (tranzactii.isEmpty())
                        System.out.println("Coada goala.");
                    else {
                        Tranzactie t = tranzactii.removeFirst();
                        System.out.println("Extras: " + t.toString());
                    }
                    break;
                }
                case "REMOVE_DEBIT": {
                    int nr = 0;
                    Iterator<Tranzactie> itr = tranzactii.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getTip().equals(TipTranzactie.DEBIT)) {
                            itr.remove();
                            nr++;
                        }
                    }
                    System.out.println("Eliminat " + nr + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    int nr = 0;
                    double threshold = Double.parseDouble(parti[1]);
                    Iterator<Tranzactie> itr = tranzactii.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getSuma() < threshold) {
                            itr.remove();
                            nr++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", nr, threshold);
                    break;
                }
                case "PRINT": {
                    for (Tranzactie t : tranzactii)
                        System.out.println(t.toString());
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " + tranzactii.size());
                    break;
                }
            }
        }
    }
}
