package br.edu.strategy;

import java.math.BigDecimal;

/** Define um algoritmo de calculo de frete intercambiavel. */
@FunctionalInterface
public interface FreteStrategy {
    /** Retorna o frete em reais para um pedido nao nulo. */
    BigDecimal calcular(Pedido pedido);
}
