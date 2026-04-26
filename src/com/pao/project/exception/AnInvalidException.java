package com.pao.project.exception;

public class AnInvalidException extends RuntimeException {
    public AnInvalidException(int an) {
        super("Anul " + an + " nu este intre 1 si anul curent");
    }
}
