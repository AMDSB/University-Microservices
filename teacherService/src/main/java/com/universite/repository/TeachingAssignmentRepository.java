package com.universite.repository;

import com.universite.domain.TeachingAssignment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TeachingAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Long> {}
