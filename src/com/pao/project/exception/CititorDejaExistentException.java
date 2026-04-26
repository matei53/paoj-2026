package com.pao.project.exception;

public class CititorDejaExistentException extends RuntimeException {
    public CititorDejaExistentException(String nume) {
        super("Cititorul cu numele " + nume + " deja exista");
    }
}