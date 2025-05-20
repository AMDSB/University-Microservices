package com.universite.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GradeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Grade getGradeSample1() {
        return new Grade().id(1L).idGrade(1);
    }

    public static Grade getGradeSample2() {
        return new Grade().id(2L).idGrade(2);
    }

    public static Grade getGradeRandomSampleGenerator() {
        return new Grade().id(longCount.incrementAndGet()).idGrade(intCount.incrementAndGet());
    }
}
