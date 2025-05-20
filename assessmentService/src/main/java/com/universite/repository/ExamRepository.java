package com.universite.repository;

import com.universite.domain.Exam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Exam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {}
