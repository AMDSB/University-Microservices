package com.universite.assessment.service.mapper;

import com.universite.assessment.domain.Exam;
import com.universite.assessment.service.dto.ExamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exam} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {}
