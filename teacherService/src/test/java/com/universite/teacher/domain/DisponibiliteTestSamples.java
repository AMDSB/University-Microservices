package com.universite.teacher.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DisponibiliteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Disponibilite getDisponibiliteSample1() {
        return new Disponibilite().id(1L).idDisponibilite(1);
    }

    public static Disponibilite getDisponibiliteSample2() {
        return new Disponibilite().id(2L).idDisponibilite(2);
    }

    public static Disponibilite getDisponibiliteRandomSampleGenerator() {
        return new Disponibilite().id(longCount.incrementAndGet()).idDisponibilite(intCount.incrementAndGet());
    }
}
