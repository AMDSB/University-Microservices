package com.universite.service.mapper;

import com.universite.domain.Departement;
import com.universite.service.dto.DepartementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {}
