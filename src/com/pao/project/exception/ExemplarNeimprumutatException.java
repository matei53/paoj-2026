package com.pao.project.exception;

import com.pao.project.model.Exemplar;

public class ExemplarNeimprumutatException extends RuntimeException {
    public ExemplarNeimprumutatException(Exemplar e) {
        super("Exemplarul " + e.cod() + " nu este imprumutat");
    }
}
