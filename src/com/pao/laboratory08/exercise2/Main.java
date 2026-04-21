package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;
import java.io.*;
import java.util.*;

public class Main {
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
        int filtru = Integer.parseInt(scanner.nextLine());

        BufferedWriter fout = new BufferedWriter(new FileWriter("src/com/pao/laboratory08/exercise2/rezultate.txt"));
        List<Student> filteredStudents = new ArrayList<Student>();
        for (Student s : students) {
            if (s.getVarsta() >= filtru) {
                filteredStudents.add(s);
                fout.write(s.toString() + "\n");
            }
        }
        fout.close();
        System.out.println("Filtru: varsta >= " + filtru);
        System.out.println("Rezultate: " + filteredStudents.size() + " studenti\n");
        for (Student s : filteredStudents)
            System.out.println(s.toString());
        System.out.println("\nScris in: rezultate.txt");
    }
}

