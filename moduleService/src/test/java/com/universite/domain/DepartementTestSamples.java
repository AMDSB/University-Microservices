package com.universite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DepartementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Departement getDepartementSample1() {
        return new Departement().id(1L).idDepartement(1).name("name1").description("description1");
    }

    public static Departement getDepartementSample2() {
        return new Departement().id(2L).idDepartement(2).name("name2").description("description2");
    }

    public static Departement getDepartementRandomSampleGenerator() {
        return new Departement()
            .id(longCount.incrementAndGet())
            .idDepartement(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
