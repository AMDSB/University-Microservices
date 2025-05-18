package com.universite.service.mapper;

import static com.universite.domain.AcademicRecordAsserts.*;
import static com.universite.domain.AcademicRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AcademicRecordMapperTest {

    private AcademicRecordMapper academicRecordMapper;

    @BeforeEach
    void setUp() {
        academicRecordMapper = new AcademicRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAcademicRecordSample1();
        var actual = academicRecordMapper.toEntity(academicRecordMapper.toDto(expected));
        assertAcademicRecordAllPropertiesEquals(expected, actual);
    }
}
