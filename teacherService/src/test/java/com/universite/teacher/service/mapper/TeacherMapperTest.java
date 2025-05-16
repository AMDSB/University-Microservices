package com.universite.teacher.service.mapper;

import static com.universite.teacher.domain.TeacherAsserts.*;
import static com.universite.teacher.domain.TeacherTestSamples.*;

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
