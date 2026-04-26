package com.pao.project.exception;

public class NivelDeteriorareException extends RuntimeException {
    public NivelDeteriorareException() {
        super("Nivelul de deteriorare poate fi intre 1 si 10");
    }
}
