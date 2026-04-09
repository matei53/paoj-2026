package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    private final OrderState stare = OrderState.PLACED;

    public abstract double pretFinal();
    public String descriere() {
        if (this instanceof ComandaStandard)
            return "STANDARD: " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei [" + stare + "]";
        else if (this instanceof ComandaRedusa)
            return "DISCOUNTED: " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei (-" +
                    ((ComandaRedusa) this).discountProcent + "%) [" + stare + "]";
        else
            return "GIFT: " + nume + ", gratuit [" + stare + "]";
    }
}
