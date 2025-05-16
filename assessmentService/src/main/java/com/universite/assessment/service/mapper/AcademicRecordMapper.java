package com.universite.assessment.service.mapper;

import com.universite.assessment.domain.AcademicRecord;
import com.universite.assessment.service.dto.AcademicRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AcademicRecord} and its DTO {@link AcademicRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface AcademicRecordMapper extends EntityMapper<AcademicRecordDTO, AcademicRecord> {}
