package com.universite.module.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ModuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Module getModuleSample1() {
        return new Module().id(1L).idModule(1).credit(1).name("name1");
    }

    public static Module getModuleSample2() {
        return new Module().id(2L).idModule(2).credit(2).name("name2");
    }

    public static Module getModuleRandomSampleGenerator() {
        return new Module()
            .id(longCount.incrementAndGet())
            .idModule(intCount.incrementAndGet())
            .credit(intCount.incrementAndGet())
            .name(UUID.randomUUID().toString());
    }
}
