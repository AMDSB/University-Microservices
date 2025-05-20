package com.universite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialisationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialisationDTO.class);
        SpecialisationDTO specialisationDTO1 = new SpecialisationDTO();
        specialisationDTO1.setId(1L);
        SpecialisationDTO specialisationDTO2 = new SpecialisationDTO();
        assertThat(specialisationDTO1).isNotEqualTo(specialisationDTO2);
        specialisationDTO2.setId(specialisationDTO1.getId());
        assertThat(specialisationDTO1).isEqualTo(specialisationDTO2);
        specialisationDTO2.setId(2L);
        assertThat(specialisationDTO1).isNotEqualTo(specialisationDTO2);
        specialisationDTO1.setId(null);
        assertThat(specialisationDTO1).isNotEqualTo(specialisationDTO2);
    }
}
