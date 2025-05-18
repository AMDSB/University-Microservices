package com.universite.domain;

import static com.universite.domain.EdtTestSamples.*;
import static com.universite.domain.FiliereTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EdtTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Edt.class);
        Edt edt1 = getEdtSample1();
        Edt edt2 = new Edt();
        assertThat(edt1).isNotEqualTo(edt2);

        edt2.setId(edt1.getId());
        assertThat(edt1).isEqualTo(edt2);

        edt2 = getEdtSample2();
        assertThat(edt1).isNotEqualTo(edt2);
    }

    @Test
    void idFiliereTest() {
        Edt edt = getEdtRandomSampleGenerator();
        Filiere filiereBack = getFiliereRandomSampleGenerator();

        edt.setIdFiliere(filiereBack);
        assertThat(edt.getIdFiliere()).isEqualTo(filiereBack);

        edt.idFiliere(null);
        assertThat(edt.getIdFiliere()).isNull();
    }
}
