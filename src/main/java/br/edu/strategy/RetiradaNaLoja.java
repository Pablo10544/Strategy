package br.edu.strategy;

import java.math.BigDecimal;
import java.util.Objects;

public final class RetiradaNaLoja implements FreteStrategy {
    @Override
    public BigDecimal calcular(Pedido pedido) {
        Objects.requireNonNull(pedido, "O pedido e obrigatorio.");
        return new BigDecimal("0.00");
    }
}
