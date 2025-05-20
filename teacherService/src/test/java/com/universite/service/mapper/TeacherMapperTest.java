package com.universite.service.mapper;

import static com.universite.domain.TeacherAsserts.*;
import static com.universite.domain.TeacherTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = new TeacherMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTeacherSample1();
        var actual = teacherMapper.toEntity(teacherMapper.toDto(expected));
        assertTeacherAllPropertiesEquals(expected, actual);
    }
}
