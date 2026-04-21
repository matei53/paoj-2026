package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        BufferedReader input = new BufferedReader(new FileReader(FILE_PATH));
        List<Student> students = new ArrayList<Student>();
        String line = input.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            students.add(new Student(parts[0], Integer.parseInt(parts[1].trim()), new Adresa(parts[2], parts[3])));
            line = input.readLine();
        }

        Scanner scanner = new Scanner(System.in);
        String instructiune = scanner.nextLine();
        if (instructiune.equals("PRINT")) {
            for (Student s : students) {
                System.out.println(s.toString());
            }
        }
        else {
            String[] parts = instructiune.split(" ", 2);
            for (Student s : students) {
                if (s.getNume().equals(parts[1])) {
                    Student c;
                    if (parts[0].equals("SHALLOW"))
                        c = (Student) s.shallowClone();
                    else
                        c = (Student) s.deepClone();
                    c.getAdresa().setOras("MODIFICAT");
                    System.out.println("Original: " + s.toString());
                    System.out.println("Clona: " + c.toString());
                }
            }
        }
    }
}
