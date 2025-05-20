package com.universite.domain;

import static com.universite.domain.ReportDetailTestSamples.*;
import static com.universite.domain.ReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.universite.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDetail.class);
        ReportDetail reportDetail1 = getReportDetailSample1();
        ReportDetail reportDetail2 = new ReportDetail();
        assertThat(reportDetail1).isNotEqualTo(reportDetail2);

        reportDetail2.setId(reportDetail1.getId());
        assertThat(reportDetail1).isEqualTo(reportDetail2);

        reportDetail2 = getReportDetailSample2();
        assertThat(reportDetail1).isNotEqualTo(reportDetail2);
    }

    @Test
    void reportTest() {
        ReportDetail reportDetail = getReportDetailRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        reportDetail.setReport(reportBack);
        assertThat(reportDetail.getReport()).isEqualTo(reportBack);

        reportDetail.report(null);
        assertThat(reportDetail.getReport()).isNull();
    }
}
