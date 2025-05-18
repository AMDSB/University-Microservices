package com.universite.service.mapper;

import com.universite.domain.Module;
import com.universite.domain.Semestre;
import com.universite.service.dto.ModuleDTO;
import com.universite.service.dto.SemestreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Semestre} and its DTO {@link SemestreDTO}.
 */
@Mapper(componentModel = "spring")
public interface SemestreMapper extends EntityMapper<SemestreDTO, Semestre> {
    @Mapping(target = "idModule", source = "idModule", qualifiedByName = "moduleId")
    SemestreDTO toDto(Semestre s);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);
}
