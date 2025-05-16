package com.universite.module.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FiliereTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Filiere getFiliereSample1() {
        return new Filiere().id(1L).idFiliere(1).name("name1").description("description1");
    }

    public static Filiere getFiliereSample2() {
        return new Filiere().id(2L).idFiliere(2).name("name2").description("description2");
    }

    public static Filiere getFiliereRandomSampleGenerator() {
        return new Filiere()
            .id(longCount.incrementAndGet())
            .idFiliere(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
