package com.universite.module.service.mapper;

import static com.universite.module.domain.SemestreAsserts.*;
import static com.universite.module.domain.SemestreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemestreMapperTest {

    private SemestreMapper semestreMapper;

    @BeforeEach
    void setUp() {
        semestreMapper = new SemestreMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSemestreSample1();
        var actual = semestreMapper.toEntity(semestreMapper.toDto(expected));
        assertSemestreAllPropertiesEquals(expected, actual);
    }
}
