package com.pao.project.exception;

public class CarteIndisponibilaException extends RuntimeException {
    public CarteIndisponibilaException() {
        super("Cartea este indisponibila");
    }
}
