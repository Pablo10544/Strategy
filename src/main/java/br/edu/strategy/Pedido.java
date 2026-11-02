package br.edu.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Contexto que delega o calculo do frete a estrategia selecionada. */
public final class Pedido {
    private final BigDecimal valorProdutos;
    private final BigDecimal pesoKg;
    private FreteStrategy freteStrategy;

    public Pedido(BigDecimal valorProdutos, BigDecimal pesoKg, FreteStrategy freteStrategy) {
        Objects.requireNonNull(valorProdutos, "O valor dos produtos e obrigatorio.");
        Objects.requireNonNull(pesoKg, "O peso e obrigatorio.");
        if (valorProdutos.signum() < 0) {
            throw new IllegalArgumentException("O valor dos produtos nao pode ser negativo.");
        }
        if (pesoKg.signum() <= 0) {
            throw new IllegalArgumentException("O peso deve ser maior que zero.");
        }
        this.valorProdutos = valorProdutos.setScale(2, RoundingMode.HALF_UP);
        this.pesoKg = pesoKg;
        setFreteStrategy(freteStrategy);
    }

    public BigDecimal getValorProdutos() {
        return valorProdutos;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public void setFreteStrategy(FreteStrategy freteStrategy) {
        this.freteStrategy = Objects.requireNonNull(freteStrategy, "A estrategia de frete e obrigatoria.");
    }

    public BigDecimal calcularFrete() {
        return freteStrategy.calcular(this).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTotal() {
        return valorProdutos.add(calcularFrete());
    }
}
