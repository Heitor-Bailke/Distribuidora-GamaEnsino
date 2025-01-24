package com.fontenova.distribuidora.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DistribuidoraTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Distribuidora getDistribuidoraSample1() {
        return new Distribuidora()
            .id(1)
            .nome("nome1")
            .cnpj("cnpj1")
            .contato(1)
            .cep("cep1")
            .cidade("cidade1")
            .bairro("bairro1")
            .rua("rua1")
            .numero(1)
            .referencia("referencia1")
            .estado("estado1")
            .detalhes("detalhes1");
    }

    public static Distribuidora getDistribuidoraSample2() {
        return new Distribuidora()
            .id(2)
            .nome("nome2")
            .cnpj("cnpj2")
            .contato(2)
            .cep("cep2")
            .cidade("cidade2")
            .bairro("bairro2")
            .rua("rua2")
            .numero(2)
            .referencia("referencia2")
            .estado("estado2")
            .detalhes("detalhes2");
    }

    public static Distribuidora getDistribuidoraRandomSampleGenerator() {
        return new Distribuidora()
            .id(intCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cnpj(UUID.randomUUID().toString())
            .contato(intCount.incrementAndGet())
            .cep(UUID.randomUUID().toString())
            .cidade(UUID.randomUUID().toString())
            .bairro(UUID.randomUUID().toString())
            .rua(UUID.randomUUID().toString())
            .numero(intCount.incrementAndGet())
            .referencia(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .detalhes(UUID.randomUUID().toString());
    }
}
