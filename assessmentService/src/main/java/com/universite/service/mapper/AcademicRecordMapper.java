package com.universite.service.mapper;

import com.universite.domain.AcademicRecord;
import com.universite.service.dto.AcademicRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AcademicRecord} and its DTO {@link AcademicRecordDTO}.
 */
@Mapper(componentModel = "spring")
public interface AcademicRecordMapper extends EntityMapper<AcademicRecordDTO, AcademicRecord> {}
