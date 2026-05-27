package com.pao.project.exception;

public class CarteNegasitaException extends RuntimeException {
    public CarteNegasitaException() {
        super("Cartea cu codul sau titlul dat nu exista");
    }
}
