package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();
        boolean running = true;
        while (running) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine().trim();
                    System.out.print("Departament (nume): ");
                    String depNume = scanner.nextLine().trim();
                    System.out.print("Departament (locatie): ");
                    String depLoc = scanner.nextLine().trim();
                    System.out.print("Salariu: ");
                    int sal = Integer.parseInt(scanner.nextLine().trim());
                    service.addAngajat(new Angajat(nume, new Departament(depNume, depLoc), sal));
                    System.out.println("Angajat adăugat: " + nume);
                    break;

                case "2":
                    System.out.print("--- Angajați după salariu (descrescător) ---\n");
                    service.listBySalary();
                    break;

                case "3":
                    System.out.print("Departament: ");
                    String dep = scanner.nextLine().trim();
                    System.out.print("--- Angajați din " + dep + " ---\n");
                    service.findByDepartament(dep);
                    break;

                case "0":
                    running = false;
                    System.out.println("La revedere!");
                    break;

                default:
                    System.out.println("Opțiune invalidă.");
            }
        }
    }
}
