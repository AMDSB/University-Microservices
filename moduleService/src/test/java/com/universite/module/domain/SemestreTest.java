package com.universite.module.domain;

import static com.universite.module.domain.ModuleTestSamples.*;
import static com.universite.module.domain.SemestreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.module.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SemestreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Semestre.class);
        Semestre semestre1 = getSemestreSample1();
        Semestre semestre2 = new Semestre();
        assertThat(semestre1).isNotEqualTo(semestre2);

        semestre2.setId(semestre1.getId());
        assertThat(semestre1).isEqualTo(semestre2);

        semestre2 = getSemestreSample2();
        assertThat(semestre1).isNotEqualTo(semestre2);
    }

    @Test
    void idModuleTest() {
        Semestre semestre = getSemestreRandomSampleGenerator();
        Module moduleBack = getModuleRandomSampleGenerator();

        semestre.setIdModule(moduleBack);
        assertThat(semestre.getIdModule()).isEqualTo(moduleBack);

        semestre.idModule(null);
        assertThat(semestre.getIdModule()).isNull();
    }
}
