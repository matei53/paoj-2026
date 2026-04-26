package com.pao.project.exception;

public class CititorNegasitException extends RuntimeException {
    public CititorNegasitException(String nume) {
        super("Cititorul cu numele " + nume + " nu exista");
    }
}
