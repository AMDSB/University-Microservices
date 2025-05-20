package com.universite.domain;

import static com.universite.domain.ModuleTestSamples.*;
import static com.universite.domain.SpecialisationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specialisation.class);
        Specialisation specialisation1 = getSpecialisationSample1();
        Specialisation specialisation2 = new Specialisation();
        assertThat(specialisation1).isNotEqualTo(specialisation2);

        specialisation2.setId(specialisation1.getId());
        assertThat(specialisation1).isEqualTo(specialisation2);

        specialisation2 = getSpecialisationSample2();
        assertThat(specialisation1).isNotEqualTo(specialisation2);
    }

    @Test
    void idModuleTest() {
        Specialisation specialisation = getSpecialisationRandomSampleGenerator();
        Module moduleBack = getModuleRandomSampleGenerator();

        specialisation.setIdModule(moduleBack);
        assertThat(specialisation.getIdModule()).isEqualTo(moduleBack);

        specialisation.idModule(null);
        assertThat(specialisation.getIdModule()).isNull();
    }
}
