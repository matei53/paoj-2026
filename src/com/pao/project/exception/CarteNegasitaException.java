package com.pao.project.exception;

import com.pao.project.model.ISBN;

public class CarteNegasitaException extends RuntimeException {
    public CarteNegasitaException() {
        super("Cartea cu codul sau titlul dat nu exista");
    }
}
