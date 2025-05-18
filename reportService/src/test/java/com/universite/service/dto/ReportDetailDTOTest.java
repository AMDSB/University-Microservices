package com.universite.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDetailDTO.class);
        ReportDetailDTO reportDetailDTO1 = new ReportDetailDTO();
        reportDetailDTO1.setId(1L);
        ReportDetailDTO reportDetailDTO2 = new ReportDetailDTO();
        assertThat(reportDetailDTO1).isNotEqualTo(reportDetailDTO2);
        reportDetailDTO2.setId(reportDetailDTO1.getId());
        assertThat(reportDetailDTO1).isEqualTo(reportDetailDTO2);
        reportDetailDTO2.setId(2L);
        assertThat(reportDetailDTO1).isNotEqualTo(reportDetailDTO2);
        reportDetailDTO1.setId(null);
        assertThat(reportDetailDTO1).isNotEqualTo(reportDetailDTO2);
    }
}
