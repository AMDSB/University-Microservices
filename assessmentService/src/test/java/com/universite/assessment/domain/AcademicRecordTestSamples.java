package com.universite.assessment.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AcademicRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AcademicRecord getAcademicRecordSample1() {
        return new AcademicRecord().id(1L).idAcademicRecord(1).mention("mention1");
    }

    public static AcademicRecord getAcademicRecordSample2() {
        return new AcademicRecord().id(2L).idAcademicRecord(2).mention("mention2");
    }

    public static AcademicRecord getAcademicRecordRandomSampleGenerator() {
        return new AcademicRecord()
            .id(longCount.incrementAndGet())
            .idAcademicRecord(intCount.incrementAndGet())
            .mention(UUID.randomUUID().toString());
    }
}
