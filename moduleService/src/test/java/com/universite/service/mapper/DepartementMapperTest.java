package com.universite.service.mapper;

import static com.universite.domain.DepartementAsserts.*;
import static com.universite.domain.DepartementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepartementMapperTest {

    private DepartementMapper departementMapper;

    @BeforeEach
    void setUp() {
        departementMapper = new DepartementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDepartementSample1();
        var actual = departementMapper.toEntity(departementMapper.toDto(expected));
        assertDepartementAllPropertiesEquals(expected, actual);
    }
}
