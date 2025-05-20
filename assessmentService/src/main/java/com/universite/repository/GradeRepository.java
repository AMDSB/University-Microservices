package com.universite.repository;

import com.universite.domain.Grade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Grade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {}
