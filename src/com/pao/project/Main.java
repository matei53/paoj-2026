package com.pao.project;

import com.pao.project.exception.*;
import com.pao.project.model.*;
import com.pao.project.service.*;
import com.pao.project.repository.*;
import com.pao.project.util.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

public class Main {
    public static void main() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ALEGE ETAPA PROIECTULUI (1/2)");
        int etapa = scanner.nextInt();
        if (etapa == 2) {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            SchemaInitializer.init(conn);

            AuditService audit = AuditService.getInstance();
            AutorRepository autorRepo = new AutorRepository();
            CategorieRepository categorieRepo = new CategorieRepository();
            CarteRepository carteRepo = new CarteRepository();
            CititorRepository cititorRepo = new CititorRepository();
            ImprumutRepository imprumutRepo = new ImprumutRepository();
            BibliotecaService bibliotecaService = BibliotecaService.getInstance();

            System.out.println("=== BIBLIOTECA JDBC - DEMO PROIECT ===\n");

            // ---- Actiunea 1: Adauga autor ----
            Autor autor = new Autor("George Calinescu");
            autorRepo.save(autor);
            audit.log("adauga_autor");
            System.out.println("1. Autor adaugat:\n" + autor);

            // ---- Actiunea 2: Adauga categorie ----
            Categorie categorie = new Categorie("Realism");
            categorieRepo.save(categorie);
            audit.log("adauga_categorie");
            System.out.println("\n2. Categorie adaugaa:\n" + categorie.getNume());

            // ---- Actiunea 3: Adauga carte ----
            Carte carte0 = new Carte("TEST", 2000, null, categorie, 1);
            Carte carte1 = new Carte("Enigma Otiliei", 1938, autor, categorie, 100);
            Carte carte2 = new Carte("Cartea nuntii", 1938, autor, categorie, 200);
            carte1.adaugaExemplar(new Exemplar(1));
            carte1.adaugaExemplar(new Exemplar(2));
            carte2.adaugaExemplar(new Exemplar(1));
            carteRepo.save(carte0);
            audit.log("adauga_carte");
            carteRepo.save(carte1);
            audit.log("adauga_carte");
            carteRepo.save(carte2);
            audit.log("adauga_carte");
            System.out.println("\n3. Carti adaugate:\n" + carte0 + "\n" + carte1 + "\n" + carte2);

            // ---- Actiunea 4: Adauga cititori ----
            Cititor cititor1 = new Cititor("Ion Popescu", "ion.popescu@email.com");
            Cititor cititor2 = new Cititor("Irina", "irina@email.com");
            cititorRepo.save(cititor1);
            audit.log("adauga_cititor");
            cititorRepo.save(cititor2);
            audit.log("adauga_cititor");
            System.out.println("\n4. Cititori adaugati: \n" + cititor1 + "\n" + cititor2);

            // ---- Actiunea 5: Listeaza toate cartile ----
            List<Carte> carti = carteRepo.findAll();
            audit.log("listeaza_cartile");
            System.out.println("\n5. Toate cartile (" + carti.size() + "):");
            carti.forEach(b -> System.out.println("   " + b));

            // ---- Actiunea 6: Cauta carte dupa id ----
            carteRepo.findById(carte1.getId()).ifPresentOrElse(
                    b -> System.out.println("\n6. Carte gasita: \n" + b),
                    () -> System.out.println("\n6. Carte negasita.")
            );
            audit.log("cauta_carte_dupa_id");

            // ---- Actiunea 7: Imprumuta carte ----
            System.out.println("\n7. Imprumuturi create");
            long id_imprumut1 = bibliotecaService.imprumutaCarte(
                    cititor1.getId(), carte1.getId(), LocalDate.now().minusDays(30));
            audit.log("imprumuta_carte");
            long id_imprumut2 = bibliotecaService.imprumutaCarte(
                    cititor1.getId(), carte2.getId(), LocalDate.now().plusDays(10));
            audit.log("imprumuta_carte");
            try {
                long id_imprumut3 = bibliotecaService.imprumutaCarte(
                        cititor2.getId(), carte2.getId(), LocalDate.now().plusDays(20));
                audit.log("imprumuta_carte");
            } catch (SQLException e) {
                audit.log("imprumuta_carte_ERROR");
            }
            long id_imprumut3 = bibliotecaService.imprumutaCarte(
                    cititor2.getId(), carte1.getId(), LocalDate.now().plusDays(20));
            audit.log("imprumuta_carte");

            // ---- Actiunea 8: Actualizeaza carte ----
            carte2.setAnLansare(1933);
            carte2.adaugaExemplar(new Exemplar(2));
            carteRepo.update(carte2);
            audit.log("actualizeaza_carte");
            System.out.println("\n8. Carte actualizata: \n" + carte2);

            // ---- Actiunea 9: Returneaza carte ----
            System.out.println("\n9. Carte returnata.");
            bibliotecaService.returneazaCarte(id_imprumut1, 8.2);
            audit.log("returneaza_carte");
            try {
                bibliotecaService.returneazaCarte(10);
                audit.log("returneaza_carte");
            } catch (SQLException e) {
                audit.log("returneaza_carte_ERROR");
            }
            try {
                bibliotecaService.returneazaCarte(id_imprumut2, -1);
                audit.log("returneaza_carte");
            } catch (SQLException e) {
                audit.log("returneaza_carte_ERROR");
            }

            // ---- Actiunea 10: Istoricul imprumuturilor unui cititor ----
            List<String> imprumuturi = bibliotecaService.istoricImprumut(cititor1.getId());
            audit.log("afiseaza_istoric_imprumuturi_cititor");
            System.out.print("\n10. Imprumuturi: " + (imprumuturi.isEmpty() ? "niciun" : ""));
            imprumuturi.forEach(s -> System.out.println("\n" + s));

            // ---- Actiunea 11: Top 5 carti imprumutate ----
            List<String> topCarti = bibliotecaService.topCartiImprumutate();
            audit.log("afiseaza_top5_carti_imprumutate");
            System.out.println("\n11. Top 5 carti imprumutate:");
            topCarti.forEach(s -> System.out.println("   " + s));

            // ---- Actiunea 12: Exemplarele disponibile ale unei carti ----
            List<Exemplar> exemplare1 = bibliotecaService.exemplareDisponibile(carte1.getId());
            audit.log("afiseaza_exemplare_disponibile");
            System.out.println("\n12. Exemplare disponibile pentru cartea cu ID=" + carte1.getId() + ": ");
            exemplare1.forEach(s -> System.out.println("   " + s));
            List<Exemplar> exemplare2 = bibliotecaService.exemplareDisponibile(carte2.getId());
            audit.log("afiseaza_exemplare_disponibile");
            System.out.println("\n   Exemplare disponibile pentru cartea cu ID=" + carte2.getId() + ": ");
            exemplare2.forEach(s -> System.out.println("   " + s));

            System.out.println("\n=== Demo finalizat. Verifica audit.csv ===");
            DatabaseConnection.getInstance().close();
        }
        else if (etapa == 1) {
            CititorService cititorService = CititorService.getInstance();
            CarteService carteService = CarteService.getInstance();

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
                                System.out.print(e.getCod() + " ");
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
                        } catch (CarteNegasitaException | CititorNegasitException | CarteIndisponibilaException e) {
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
}