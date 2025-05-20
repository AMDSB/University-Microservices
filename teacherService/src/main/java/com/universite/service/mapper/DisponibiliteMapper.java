package com.universite.service.mapper;

import com.universite.domain.Disponibilite;
import com.universite.service.dto.DisponibiliteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Disponibilite} and its DTO {@link DisponibiliteDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisponibiliteMapper extends EntityMapper<DisponibiliteDTO, Disponibilite> {}
