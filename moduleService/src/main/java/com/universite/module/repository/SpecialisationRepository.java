package com.universite.module.repository;

import com.universite.module.domain.Specialisation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Specialisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialisationRepository extends JpaRepository<Specialisation, Long> {}
