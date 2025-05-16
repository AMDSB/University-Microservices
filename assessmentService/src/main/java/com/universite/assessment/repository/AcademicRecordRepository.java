package com.universite.assessment.repository;

import com.universite.assessment.domain.AcademicRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AcademicRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {}
