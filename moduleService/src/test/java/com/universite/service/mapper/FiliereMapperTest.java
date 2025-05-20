package com.universite.service.mapper;

import static com.universite.domain.FiliereAsserts.*;
import static com.universite.domain.FiliereTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FiliereMapperTest {

    private FiliereMapper filiereMapper;

    @BeforeEach
    void setUp() {
        filiereMapper = new FiliereMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFiliereSample1();
        var actual = filiereMapper.toEntity(filiereMapper.toDto(expected));
        assertFiliereAllPropertiesEquals(expected, actual);
    }
}
