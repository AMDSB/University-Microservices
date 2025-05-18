package com.universite.service.mapper;

import com.universite.domain.Grade;
import com.universite.service.dto.GradeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Grade} and its DTO {@link GradeDTO}.
 */
@Mapper(componentModel = "spring")
public interface GradeMapper extends EntityMapper<GradeDTO, Grade> {}
