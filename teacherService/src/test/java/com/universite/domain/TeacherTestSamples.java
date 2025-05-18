package com.universite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TeacherTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Teacher getTeacherSample1() {
        return new Teacher().id(1L).idTeacher(1).grade("grade1").specialite("specialite1");
    }

    public static Teacher getTeacherSample2() {
        return new Teacher().id(2L).idTeacher(2).grade("grade2").specialite("specialite2");
    }

    public static Teacher getTeacherRandomSampleGenerator() {
        return new Teacher()
            .id(longCount.incrementAndGet())
            .idTeacher(intCount.incrementAndGet())
            .grade(UUID.randomUUID().toString())
            .specialite(UUID.randomUUID().toString());
    }
}
