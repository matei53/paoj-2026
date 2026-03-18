package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {
    List<Student> students;

    private static StudentService instance;
    private StudentService() {
        this.students = new ArrayList<>();
    }
    public static StudentService getInstance() {
        return new StudentService();
    }

    public void addStudent(String name, int age){
        for (Student s : students)
            if (Objects.equals(s.getName(), name))
                throw new RuntimeException("Studentul cu numele " + name + " deja exista");
        students.add(new Student(name, age));
    }
    public Student findByName(String name){
        for (Student s : students)
            if (Objects.equals(s.getName(), name))
                return s;
        throw new StudentNotFoundException("Studentul cu numele " + name + " nu exista");
    }
    public void addGrade(String studentName, Subject subject, double grade){
        findByName(studentName).addGrade(subject, grade);
    }
    public void printAllStudents(){
        int i = 1;
        for (Student s : students){
            System.out.println(i + ". " + s.toString());
            for (Map.Entry<Subject, Double> g : s.getGrades().entrySet()){
                System.out.println("   " + g.getKey() + " = " + g.getValue());
            }
            i++;
        }
    }
    public void printTopStudents(){
        List<Student> sortedStudents = new ArrayList<>(students);
        sortedStudents.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));
        int i = 1;
        for (Student s : sortedStudents){
            System.out.println(i + ". " + s.getName() + " - media: " + s.getAverage());
            i++;
        }
    }
    public Map<Subject, Double> getAveragePerSubject(){
        Map<Subject, Double> result = new HashMap<>();
        for (Subject subject : Subject.values()) {
            // colectează note de la toți studenții care au notă la 'subject'
            int nr = 0;
            double gradeSum = 0;
            for (Student s : students){
                for (Map.Entry<Subject, Double> g : s.getGrades().entrySet()) {
                    if (g.getKey() == subject){
                        gradeSum += g.getValue();
                        nr++;
                    }
                }
            }
            result.put(subject, gradeSum / nr);
        }
        return result;
    }
}
