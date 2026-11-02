package br.edu.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class FreteExpresso implements FreteStrategy {
    private static final BigDecimal TAXA_FIXA = new BigDecimal("20.00");
    private static final BigDecimal VALOR_POR_KG = new BigDecimal("5.00");

    @Override
    public BigDecimal calcular(Pedido pedido) {
        Objects.requireNonNull(pedido, "O pedido e obrigatorio.");
        return TAXA_FIXA.add(pedido.getPesoKg().multiply(VALOR_POR_KG))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
