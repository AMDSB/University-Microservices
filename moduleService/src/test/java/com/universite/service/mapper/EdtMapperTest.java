package com.universite.service.mapper;

import static com.universite.domain.EdtAsserts.*;
import static com.universite.domain.EdtTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EdtMapperTest {

    private EdtMapper edtMapper;

    @BeforeEach
    void setUp() {
        edtMapper = new EdtMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEdtSample1();
        var actual = edtMapper.toEntity(edtMapper.toDto(expected));
        assertEdtAllPropertiesEquals(expected, actual);
    }
}
