package com.universite.teacher.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.universite.teacher.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisponibiliteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisponibiliteDTO.class);
        DisponibiliteDTO disponibiliteDTO1 = new DisponibiliteDTO();
        disponibiliteDTO1.setId(1L);
        DisponibiliteDTO disponibiliteDTO2 = new DisponibiliteDTO();
        assertThat(disponibiliteDTO1).isNotEqualTo(disponibiliteDTO2);
        disponibiliteDTO2.setId(disponibiliteDTO1.getId());
        assertThat(disponibiliteDTO1).isEqualTo(disponibiliteDTO2);
        disponibiliteDTO2.setId(2L);
        assertThat(disponibiliteDTO1).isNotEqualTo(disponibiliteDTO2);
        disponibiliteDTO1.setId(null);
        assertThat(disponibiliteDTO1).isNotEqualTo(disponibiliteDTO2);
    }
}
