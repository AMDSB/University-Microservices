package com.universite.service.mapper;

import com.universite.domain.Teacher;
import com.universite.domain.TeachingAssignment;
import com.universite.service.dto.TeacherDTO;
import com.universite.service.dto.TeachingAssignmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeachingAssignment} and its DTO {@link TeachingAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeachingAssignmentMapper extends EntityMapper<TeachingAssignmentDTO, TeachingAssignment> {
    @Mapping(target = "idTeacher", source = "idTeacher", qualifiedByName = "teacherId")
    TeachingAssignmentDTO toDto(TeachingAssignment s);

    @Named("teacherId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeacherDTO toDtoTeacherId(Teacher teacher);
}
