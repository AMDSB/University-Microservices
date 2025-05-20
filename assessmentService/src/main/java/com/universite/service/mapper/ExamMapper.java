package com.universite.service.mapper;

import com.universite.domain.Exam;
import com.universite.service.dto.ExamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exam} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {}
