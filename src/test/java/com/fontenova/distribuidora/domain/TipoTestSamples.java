package com.fontenova.distribuidora.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TipoTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Tipo getTipoSample1() {
        return new Tipo().id(1).name("name1");
    }

    public static Tipo getTipoSample2() {
        return new Tipo().id(2).name("name2");
    }

    public static Tipo getTipoRandomSampleGenerator() {
        return new Tipo().id(intCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
