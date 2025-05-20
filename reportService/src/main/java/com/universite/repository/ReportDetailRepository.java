package com.universite.repository;

import com.universite.domain.ReportDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportDetailRepository extends JpaRepository<ReportDetail, Long> {}
