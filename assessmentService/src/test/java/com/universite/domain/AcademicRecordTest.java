package com.universite.domain;

import static com.universite.domain.AcademicRecordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcademicRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicRecord.class);
        AcademicRecord academicRecord1 = getAcademicRecordSample1();
        AcademicRecord academicRecord2 = new AcademicRecord();
        assertThat(academicRecord1).isNotEqualTo(academicRecord2);

        academicRecord2.setId(academicRecord1.getId());
        assertThat(academicRecord1).isEqualTo(academicRecord2);

        academicRecord2 = getAcademicRecordSample2();
        assertThat(academicRecord1).isNotEqualTo(academicRecord2);
    }
}
