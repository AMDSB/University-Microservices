package com.universite.service.mapper;

import static com.universite.domain.SpecialisationAsserts.*;
import static com.universite.domain.SpecialisationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecialisationMapperTest {

    private SpecialisationMapper specialisationMapper;

    @BeforeEach
    void setUp() {
        specialisationMapper = new SpecialisationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSpecialisationSample1();
        var actual = specialisationMapper.toEntity(specialisationMapper.toDto(expected));
        assertSpecialisationAllPropertiesEquals(expected, actual);
    }
}
