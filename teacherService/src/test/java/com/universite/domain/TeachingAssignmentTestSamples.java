package com.universite.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TeachingAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TeachingAssignment getTeachingAssignmentSample1() {
        return new TeachingAssignment().id(1L).idTeachingAssignment(1).academicYear(1);
    }

    public static TeachingAssignment getTeachingAssignmentSample2() {
        return new TeachingAssignment().id(2L).idTeachingAssignment(2).academicYear(2);
    }

    public static TeachingAssignment getTeachingAssignmentRandomSampleGenerator() {
        return new TeachingAssignment()
            .id(longCount.incrementAndGet())
            .idTeachingAssignment(intCount.incrementAndGet())
            .academicYear(intCount.incrementAndGet());
    }
}
