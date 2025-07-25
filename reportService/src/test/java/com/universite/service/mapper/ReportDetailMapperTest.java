package com.universite.service.mapper;

import static com.universite.domain.ReportDetailAsserts.*;
import static com.universite.domain.ReportDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportDetailMapperTest {

    private ReportDetailMapper reportDetailMapper;

    @BeforeEach
    void setUp() {
        reportDetailMapper = new ReportDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReportDetailSample1();
        var actual = reportDetailMapper.toEntity(reportDetailMapper.toDto(expected));
        assertReportDetailAllPropertiesEquals(expected, actual);
    }
}
