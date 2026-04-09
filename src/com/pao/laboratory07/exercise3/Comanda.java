package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    private final OrderState stare = OrderState.PLACED;
    protected String client;

    public String getClient() {
        return client;
    }

    public abstract double pretFinal();
    public abstract String getType();
    public String descriere() {
        if (this instanceof ComandaStandard)
            return getType() + ": " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei [" + stare +
                    "] - client: " + client;
        else if (this instanceof ComandaRedusa)
            return getType() + ": " + nume + ", pret: " + String.format("%.2f", pretFinal()) + " lei (-" +
                    ((ComandaRedusa) this).discountProcent + "%) [" + stare + "] - client: " + client;
        else
            return getType() + ": " + nume + ", gratuit [" + stare + "] - client: " + client;
    }
}
