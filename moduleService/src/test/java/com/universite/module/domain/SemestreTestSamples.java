package com.universite.module.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SemestreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Semestre getSemestreSample1() {
        return new Semestre().id(1L).idSemestre(1).name("name1");
    }

    public static Semestre getSemestreSample2() {
        return new Semestre().id(2L).idSemestre(2).name("name2");
    }

    public static Semestre getSemestreRandomSampleGenerator() {
        return new Semestre().id(longCount.incrementAndGet()).idSemestre(intCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
