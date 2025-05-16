package com.universite.module.service.mapper;

import static com.universite.module.domain.SpecialisationAsserts.*;
import static com.universite.module.domain.SpecialisationTestSamples.*;

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
