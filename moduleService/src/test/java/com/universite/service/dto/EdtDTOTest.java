package com.universite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EdtDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EdtDTO.class);
        EdtDTO edtDTO1 = new EdtDTO();
        edtDTO1.setId(1L);
        EdtDTO edtDTO2 = new EdtDTO();
        assertThat(edtDTO1).isNotEqualTo(edtDTO2);
        edtDTO2.setId(edtDTO1.getId());
        assertThat(edtDTO1).isEqualTo(edtDTO2);
        edtDTO2.setId(2L);
        assertThat(edtDTO1).isNotEqualTo(edtDTO2);
        edtDTO1.setId(null);
        assertThat(edtDTO1).isNotEqualTo(edtDTO2);
    }
}
