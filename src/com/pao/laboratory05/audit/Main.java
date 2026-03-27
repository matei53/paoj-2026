package com.pao.laboratory05.audit;

import java.util.Scanner;

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
            System.out.println("4. Gestionare Angajați (cu Audit)");
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

                case "4":
                    System.out.print("--- Audit Log ---\n");
                    service.printAuditLog();
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
