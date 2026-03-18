package com.pao.laboratory03.exercise.model;

import com.pao.laboratory03.exercise.exception.InvalidGradeException;
import com.pao.laboratory03.exercise.exception.InvalidStudentException;

import java.util.HashMap;
import java.util.Map;

public class Student {
    String name;
    int age;
    Map<Subject, Double> grades;

    public Student(String name, int age){
        if (age < 18 || age > 60) throw new InvalidStudentException("Vârsta " + age + " nu este validă (18-60)");
        this.name = name;
        this.age = age;
        grades = new HashMap<>();
    }

    public String getName(){
        return this.name;
    }
    public int getAge(){
        return this.age;
    }
    public Map<Subject, Double> getGrades(){
        return this.grades;
    }

    public void addGrade(Subject subject, double grade){
        if (grade < 1 || grade > 10) throw new InvalidGradeException("Nota " + grade + " nu este validă (1-10)");
        grades.put(subject, grade);
    }

    public double getAverage() {
        if (grades.isEmpty()) return 0;
        double sum = 0;
        for (double g : grades.values()) sum += g;
        return sum / grades.size();
    }

    @Override
    public String toString(){
        return "Student{name='" + this.name + "', age=" + this.age + ", avg=" + getAverage() + "}";
    }
}
