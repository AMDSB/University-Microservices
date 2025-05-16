package com.universite.teacher.service.mapper;

import static com.universite.teacher.domain.TeachingAssignmentAsserts.*;
import static com.universite.teacher.domain.TeachingAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeachingAssignmentMapperTest {

    private TeachingAssignmentMapper teachingAssignmentMapper;

    @BeforeEach
    void setUp() {
        teachingAssignmentMapper = new TeachingAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTeachingAssignmentSample1();
        var actual = teachingAssignmentMapper.toEntity(teachingAssignmentMapper.toDto(expected));
        assertTeachingAssignmentAllPropertiesEquals(expected, actual);
    }
}
