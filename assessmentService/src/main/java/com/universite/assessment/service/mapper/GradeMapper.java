package com.universite.assessment.service.mapper;

import com.universite.assessment.domain.Grade;
import com.universite.assessment.service.dto.GradeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Grade} and its DTO {@link GradeDTO}.
 */
@Mapper(componentModel = "spring")
public interface GradeMapper extends EntityMapper<GradeDTO, Grade> {}
