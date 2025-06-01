package com.universite.domain;

import static com.universite.domain.TeacherTestSamples.*;
import static com.universite.domain.TeachingAssignmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeachingAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeachingAssignment.class);
        TeachingAssignment teachingAssignment1 = getTeachingAssignmentSample1();
        TeachingAssignment teachingAssignment2 = new TeachingAssignment();
        assertThat(teachingAssignment1).isNotEqualTo(teachingAssignment2);

        teachingAssignment2.setId(teachingAssignment1.getId());
        assertThat(teachingAssignment1).isEqualTo(teachingAssignment2);

        teachingAssignment2 = getTeachingAssignmentSample2();
        assertThat(teachingAssignment1).isNotEqualTo(teachingAssignment2);
    }

    @Test
    void idTeacherTest() {
        TeachingAssignment teachingAssignment = getTeachingAssignmentRandomSampleGenerator();
        Teacher teacherBack = getTeacherRandomSampleGenerator();

        teachingAssignment.setIdTeacher(teacherBack);
        assertThat(teachingAssignment.getIdTeacher()).isEqualTo(teacherBack);

        teachingAssignment.idTeacher(null);
        assertThat(teachingAssignment.getIdTeacher()).isNull();
    }
}
