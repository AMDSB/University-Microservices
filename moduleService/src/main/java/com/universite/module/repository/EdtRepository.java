package com.universite.module.repository;

import com.universite.module.domain.Edt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Edt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EdtRepository extends JpaRepository<Edt, Long> {}
