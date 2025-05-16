package com.universite.module.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialisationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Specialisation getSpecialisationSample1() {
        return new Specialisation().id(1L).idSpecialisation(1).name("name1");
    }

    public static Specialisation getSpecialisationSample2() {
        return new Specialisation().id(2L).idSpecialisation(2).name("name2");
    }

    public static Specialisation getSpecialisationRandomSampleGenerator() {
        return new Specialisation()
            .id(longCount.incrementAndGet())
            .idSpecialisation(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString());
    }
}
