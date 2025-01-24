package com.fontenova.distribuidora.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ProdutoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Produto getProdutoSample1() {
        return new Produto().id(1).nome("nome1").quantidade(1);
    }

    public static Produto getProdutoSample2() {
        return new Produto().id(2).nome("nome2").quantidade(2);
    }

    public static Produto getProdutoRandomSampleGenerator() {
        return new Produto().id(intCount.incrementAndGet()).nome(UUID.randomUUID().toString()).quantidade(intCount.incrementAndGet());
    }
}
