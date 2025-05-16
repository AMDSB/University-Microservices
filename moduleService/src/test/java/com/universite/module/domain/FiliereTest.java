package com.universite.module.domain;

import static com.universite.module.domain.DepartementTestSamples.*;
import static com.universite.module.domain.FiliereTestSamples.*;
import static com.universite.module.domain.ModuleTestSamples.*;
import static com.universite.module.domain.SpecialisationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.module.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FiliereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filiere.class);
        Filiere filiere1 = getFiliereSample1();
        Filiere filiere2 = new Filiere();
        assertThat(filiere1).isNotEqualTo(filiere2);

        filiere2.setId(filiere1.getId());
        assertThat(filiere1).isEqualTo(filiere2);

        filiere2 = getFiliereSample2();
        assertThat(filiere1).isNotEqualTo(filiere2);
    }

    @Test
    void idModuleTest() {
        Filiere filiere = getFiliereRandomSampleGenerator();
        Module moduleBack = getModuleRandomSampleGenerator();

        filiere.setIdModule(moduleBack);
        assertThat(filiere.getIdModule()).isEqualTo(moduleBack);

        filiere.idModule(null);
        assertThat(filiere.getIdModule()).isNull();
    }

    @Test
    void idDepartementTest() {
        Filiere filiere = getFiliereRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        filiere.setIdDepartement(departementBack);
        assertThat(filiere.getIdDepartement()).isEqualTo(departementBack);

        filiere.idDepartement(null);
        assertThat(filiere.getIdDepartement()).isNull();
    }

    @Test
    void idSpecialisationTest() {
        Filiere filiere = getFiliereRandomSampleGenerator();
        Specialisation specialisationBack = getSpecialisationRandomSampleGenerator();

        filiere.setIdSpecialisation(specialisationBack);
        assertThat(filiere.getIdSpecialisation()).isEqualTo(specialisationBack);

        filiere.idSpecialisation(null);
        assertThat(filiere.getIdSpecialisation()).isNull();
    }
}
