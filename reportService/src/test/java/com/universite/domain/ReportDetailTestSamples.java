package com.universite.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReportDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReportDetail getReportDetailSample1() {
        return new ReportDetail().id(1L).moduleName("moduleName1").numberOfStudents(1).numberPassed(1).numberFailed(1);
    }

    public static ReportDetail getReportDetailSample2() {
        return new ReportDetail().id(2L).moduleName("moduleName2").numberOfStudents(2).numberPassed(2).numberFailed(2);
    }

    public static ReportDetail getReportDetailRandomSampleGenerator() {
        return new ReportDetail()
            .id(longCount.incrementAndGet())
            .moduleName(UUID.randomUUID().toString())
            .numberOfStudents(intCount.incrementAndGet())
            .numberPassed(intCount.incrementAndGet())
            .numberFailed(intCount.incrementAndGet());
    }
}
