package com.universite.module.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EdtTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Edt getEdtSample1() {
        return new Edt().id(1L).idEdt(1).duration("duration1");
    }

    public static Edt getEdtSample2() {
        return new Edt().id(2L).idEdt(2).duration("duration2");
    }

    public static Edt getEdtRandomSampleGenerator() {
        return new Edt().id(longCount.incrementAndGet()).idEdt(intCount.incrementAndGet()).duration(UUID.randomUUID().toString());
    }
}
