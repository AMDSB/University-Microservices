package com.universite.repository;

import com.universite.domain.Filiere;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Filiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Long> {}
