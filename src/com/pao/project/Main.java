package com.pao.project;

import com.pao.project.exception.*;
import com.pao.project.model.*;
import com.pao.project.service.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;

public class Main {
    public static void main() {
        CititorService cititorService = CititorService.getInstance();
        CarteService carteService = CarteService.getInstance();

        Scanner scanner = new Scanner(System.in);
        int actiune = -1;

        while (actiune != 0) {
            System.out.println("\n===== MENIU =====");
            System.out.println(" 1. Adauga carte");
            System.out.println(" 2. Inregistreaza cititor");
            System.out.println(" 3. Elimina cititor");
            System.out.println(" 4. Cauta carti dupa autor");
            System.out.println(" 5. Cauta carti dupa categorie");
            System.out.println(" 6. Verifica disponibilitatea unei carti");
            System.out.println(" 7. Imprumuta o carte unui cititor");
            System.out.println(" 8. Returneaza o carte");
            System.out.println(" 9. Afiseaza istoricul imprumuturilor unui cititor");
            System.out.println("10. Afiseaza primele 5 carti cu cele mai multe imprumuturi");
            System.out.println(" 0. Iesire");
            System.out.println("================");

            System.out.print("Insereaza: ");
            actiune = scanner.nextInt();

            switch (actiune) {
                case 1:
                    try {
                        System.out.print("Titlu: ");
                        String titlu = scanner.next();
                        System.out.print("An lansare: ");
                        int anLansare = scanner.nextInt();
                        if (anLansare > Year.now().getValue() || anLansare < 1) {
                            throw new AnInvalidException(anLansare);
                        }
                        System.out.print("Autor: ");
                        Autor autor = new Autor(scanner.next());
                        System.out.print("Categorie: ");
                        Categorie categorie = new Categorie(scanner.next());
                        System.out.print("Numar de pagini: ");
                        int nrPagini = scanner.nextInt();
                        System.out.print("Numar de exemplare: ");
                        int nrExemplare = scanner.nextInt();

                        carteService.adaugaCarte(new Carte(titlu, anLansare, autor, categorie, nrPagini), nrExemplare);
                    } catch (AnInvalidException | NumarExemplareInvalid e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        System.out.print("Nume: ");
                        String nume = scanner.next();
                        System.out.print("Email: ");
                        String email = scanner.next();
                        Cititor c = new Cititor(nume, email);
                        cititorService.adaugaCititor(c);
                    } catch (CititorDejaExistentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Nume: ");
                    String numeCititor = scanner.next();
                    cititorService.stergeCititor(new Cititor(numeCititor, null));
                    break;
                case 4:
                    System.out.print("Nume: ");
                    String numeAutor = scanner.next();
                    for (Carte c : carteService.cautaDupaAutor(new Autor(numeAutor))) {
                        System.out.println(c.toString());
                    }
                    break;
                case 5:
                    System.out.print("Nume: ");
                    String numeCategorie = scanner.next();
                    for (Carte c : carteService.cautaDupaCategorie(new Categorie(numeCategorie))) {
                        System.out.println(c.toString());
                    }
                    break;
                case 6:
                    try {
                        System.out.print("Cod ISBN: ");
                        int codISBN = scanner.nextInt();
                        System.out.println("Exemplarele disponibile: ");
                        for (Exemplar e : carteService.carteDisponibila(new ISBN(codISBN))) {
                            System.out.print(e.cod() + " ");
                        }
                        System.out.print("\n");
                    } catch (CarteIndisponibilaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    try {
                        System.out.print("Cod ISBN carte: ");
                        ISBN codISBNImprumut = new ISBN(scanner.nextInt());
                        System.out.print("Nume cititor: ");
                        String numeCititorImprumut = scanner.next();
                        System.out.print("Data imprumut: (yyyy-mm-dd) ");
                        LocalDate dataImprumut = LocalDate.parse(scanner.next());
                        System.out.print("Data scadenta: (yyyy-mm-dd) ");
                        LocalDate dataScadenta = LocalDate.parse(scanner.next());
                        carteService.adaugaImprumut(codISBNImprumut,
                                cititorService.cautaDupaNume(numeCititorImprumut), dataImprumut, dataScadenta);
                    }
                    catch (CarteNegasitaException | CititorNegasitException | CarteIndisponibilaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 8:
                    try {
                        System.out.print("Cod exemplar carte: ");
                        Exemplar codExemplar = new Exemplar(scanner.nextInt());
                        carteService.returneazaImprumut(codExemplar);
                    } catch (ExemplarNeimprumutatException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 9:
                    try {
                        System.out.print("Nume: ");
                        String nume = scanner.next();
                        carteService.afiseazaIstoricImprumuturiCititor(cititorService.cautaDupaNume(nume));
                    } catch (CititorNegasitException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 10:
                    carteService.topCartiImprumutate();
                    break;
                case 0:
                    System.out.println("Finalizare");
                    break;
                default:
                    System.out.println("Alegere invalida");
                    break;
            }
        }
    }
}