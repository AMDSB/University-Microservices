package com.universite.report.repository;

import com.universite.report.domain.ReportDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportDetailRepository extends JpaRepository<ReportDetail, Long> {}
