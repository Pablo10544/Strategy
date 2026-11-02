package br.edu.strategy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {
    @Test
    void calculaFreteNormalETotal() {
        Pedido pedido = novoPedido(new FreteNormal());

        assertEquals(new BigDecimal("14.00"), pedido.calcularFrete());
        assertEquals(new BigDecimal("114.00"), pedido.calcularTotal());
    }

    @Test
    void calculaFreteExpressoETotal() {
        Pedido pedido = novoPedido(new FreteExpresso());

        assertEquals(new BigDecimal("30.00"), pedido.calcularFrete());
        assertEquals(new BigDecimal("130.00"), pedido.calcularTotal());
    }

    @Test
    void calculaRetiradaNaLojaETotal() {
        Pedido pedido = novoPedido(new RetiradaNaLoja());

        assertEquals(new BigDecimal("0.00"), pedido.calcularFrete());
        assertEquals(new BigDecimal("100.00"), pedido.calcularTotal());
    }

    @Test
    void trocaEstrategiaNoMesmoPedido() {
        Pedido pedido = novoPedido(new FreteNormal());
        assertEquals(new BigDecimal("14.00"), pedido.calcularFrete());

        pedido.setFreteStrategy(new FreteExpresso());
        assertEquals(new BigDecimal("130.00"), pedido.calcularTotal());

        pedido.setFreteStrategy(new RetiradaNaLoja());
        assertEquals(new BigDecimal("100.00"), pedido.calcularTotal());
        assertEquals(new BigDecimal("100.00"), pedido.getValorProdutos());
        assertEquals(new BigDecimal("2"), pedido.getPesoKg());
    }

    @Test
    void aceitaNovoAlgoritmoSemAlterarOContexto() {
        Pedido pedido = novoPedido(dados -> dados.getValorProdutos().multiply(new BigDecimal("0.10")));

        assertEquals(new BigDecimal("10.00"), pedido.calcularFrete());
        assertEquals(new BigDecimal("110.00"), pedido.calcularTotal());
    }

    @Test
    void arredondaFreteComPesoFracionario() {
        Pedido pedido = new Pedido(new BigDecimal("100.00"), new BigDecimal("0.333"), new FreteNormal());
        assertEquals(new BigDecimal("10.67"), pedido.calcularFrete());

        pedido.setFreteStrategy(new FreteExpresso());
        assertEquals(new BigDecimal("21.67"), pedido.calcularFrete());
        assertEquals(new BigDecimal("121.67"), pedido.calcularTotal());
    }

    @Test
    void arredondaValorDosProdutosParaCentavos() {
        Pedido pedido = new Pedido(new BigDecimal("10.005"), BigDecimal.ONE, new RetiradaNaLoja());

        assertEquals(new BigDecimal("10.01"), pedido.getValorProdutos());
        assertEquals(new BigDecimal("10.01"), pedido.calcularTotal());
    }

    @Test
    void permiteValorDosProdutosIgualAZero() {
        Pedido pedido = new Pedido(BigDecimal.ZERO, BigDecimal.ONE, new FreteNormal());

        assertEquals(new BigDecimal("12.00"), pedido.calcularTotal());
    }

    @Test
    void rejeitaPesoZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(BigDecimal.TEN, BigDecimal.ZERO, new FreteNormal()));
    }

    @Test
    void rejeitaPesoNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(BigDecimal.TEN, new BigDecimal("-1"), new FreteNormal()));
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(BigDecimal.TEN, new BigDecimal("-0.001"), new FreteNormal()));
    }

    @Test
    void rejeitaValorNegativoMesmoAntesDoArredondamento() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(new BigDecimal("-0.001"), BigDecimal.ONE, new FreteNormal()));
    }

    @Test
    void rejeitaDadosNulosNoConstrutor() {
        assertThrows(NullPointerException.class,
                () -> new Pedido(null, BigDecimal.ONE, new FreteNormal()));
        assertThrows(NullPointerException.class,
                () -> new Pedido(BigDecimal.TEN, null, new FreteNormal()));
        assertThrows(NullPointerException.class,
                () -> new Pedido(BigDecimal.TEN, BigDecimal.ONE, null));
    }

    @Test
    void rejeitaTrocaPorEstrategiaNulaEPreservaAEstrategiaAtual() {
        Pedido pedido = novoPedido(new FreteNormal());

        assertThrows(NullPointerException.class, () -> pedido.setFreteStrategy(null));
        assertEquals(new BigDecimal("14.00"), pedido.calcularFrete());
    }

    @Test
    void estrategiasRejeitamPedidoNulo() {
        assertThrows(NullPointerException.class, () -> new FreteNormal().calcular(null));
        assertThrows(NullPointerException.class, () -> new FreteExpresso().calcular(null));
        assertThrows(NullPointerException.class, () -> new RetiradaNaLoja().calcular(null));
    }

    private Pedido novoPedido(FreteStrategy estrategia) {
        return new Pedido(new BigDecimal("100.00"), new BigDecimal("2"), estrategia);
    }
}
