package com.universite.teacher.service.mapper;

import static com.universite.teacher.domain.DisponibiliteAsserts.*;
import static com.universite.teacher.domain.DisponibiliteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisponibiliteMapperTest {

    private DisponibiliteMapper disponibiliteMapper;

    @BeforeEach
    void setUp() {
        disponibiliteMapper = new DisponibiliteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDisponibiliteSample1();
        var actual = disponibiliteMapper.toEntity(disponibiliteMapper.toDto(expected));
        assertDisponibiliteAllPropertiesEquals(expected, actual);
    }
}
