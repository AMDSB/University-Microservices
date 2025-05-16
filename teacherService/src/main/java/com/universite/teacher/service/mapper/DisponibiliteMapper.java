package com.universite.teacher.service.mapper;

import com.universite.teacher.domain.Disponibilite;
import com.universite.teacher.service.dto.DisponibiliteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disponibilite} and its DTO {@link DisponibiliteDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisponibiliteMapper extends EntityMapper<DisponibiliteDTO, Disponibilite> {}
