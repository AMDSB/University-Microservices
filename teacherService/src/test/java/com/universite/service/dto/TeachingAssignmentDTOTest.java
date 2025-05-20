package com.universite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeachingAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeachingAssignmentDTO.class);
        TeachingAssignmentDTO teachingAssignmentDTO1 = new TeachingAssignmentDTO();
        teachingAssignmentDTO1.setId(1L);
        TeachingAssignmentDTO teachingAssignmentDTO2 = new TeachingAssignmentDTO();
        assertThat(teachingAssignmentDTO1).isNotEqualTo(teachingAssignmentDTO2);
        teachingAssignmentDTO2.setId(teachingAssignmentDTO1.getId());
        assertThat(teachingAssignmentDTO1).isEqualTo(teachingAssignmentDTO2);
        teachingAssignmentDTO2.setId(2L);
        assertThat(teachingAssignmentDTO1).isNotEqualTo(teachingAssignmentDTO2);
        teachingAssignmentDTO1.setId(null);
        assertThat(teachingAssignmentDTO1).isNotEqualTo(teachingAssignmentDTO2);
    }
}
