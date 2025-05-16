package com.universite.teacher.service.mapper;

import com.universite.teacher.domain.Disponibilite;
import com.universite.teacher.domain.Teacher;
import com.universite.teacher.service.dto.DisponibiliteDTO;
import com.universite.teacher.service.dto.TeacherDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Teacher} and its DTO {@link TeacherDTO}.
 */
@Mapper(componentModel = "spring")
public interface TeacherMapper extends EntityMapper<TeacherDTO, Teacher> {
    @Mapping(target = "idDisponibilite", source = "idDisponibilite", qualifiedByName = "disponibiliteId")
    TeacherDTO toDto(Teacher s);

    @Named("disponibiliteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DisponibiliteDTO toDtoDisponibiliteId(Disponibilite disponibilite);
}
