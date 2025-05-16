package com.universite.teacher.domain;

import static com.universite.teacher.domain.DisponibiliteTestSamples.*;
import static com.universite.teacher.domain.TeacherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.teacher.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeacherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Teacher.class);
        Teacher teacher1 = getTeacherSample1();
        Teacher teacher2 = new Teacher();
        assertThat(teacher1).isNotEqualTo(teacher2);

        teacher2.setId(teacher1.getId());
        assertThat(teacher1).isEqualTo(teacher2);

        teacher2 = getTeacherSample2();
        assertThat(teacher1).isNotEqualTo(teacher2);
    }

    @Test
    void idDisponibiliteTest() {
        Teacher teacher = getTeacherRandomSampleGenerator();
        Disponibilite disponibiliteBack = getDisponibiliteRandomSampleGenerator();

        teacher.setIdDisponibilite(disponibiliteBack);
        assertThat(teacher.getIdDisponibilite()).isEqualTo(disponibiliteBack);

        teacher.idDisponibilite(null);
        assertThat(teacher.getIdDisponibilite()).isNull();
    }
}
