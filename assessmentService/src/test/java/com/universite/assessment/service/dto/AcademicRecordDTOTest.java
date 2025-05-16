package com.universite.assessment.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.universite.assessment.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcademicRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicRecordDTO.class);
        AcademicRecordDTO academicRecordDTO1 = new AcademicRecordDTO();
        academicRecordDTO1.setId(1L);
        AcademicRecordDTO academicRecordDTO2 = new AcademicRecordDTO();
        assertThat(academicRecordDTO1).isNotEqualTo(academicRecordDTO2);
        academicRecordDTO2.setId(academicRecordDTO1.getId());
        assertThat(academicRecordDTO1).isEqualTo(academicRecordDTO2);
        academicRecordDTO2.setId(2L);
        assertThat(academicRecordDTO1).isNotEqualTo(academicRecordDTO2);
        academicRecordDTO1.setId(null);
        assertThat(academicRecordDTO1).isNotEqualTo(academicRecordDTO2);
    }
}
