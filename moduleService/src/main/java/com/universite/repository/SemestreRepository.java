package com.universite.repository;

import com.universite.domain.Semestre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Semestre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Long> {}
