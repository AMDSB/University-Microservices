package com.universite.module.service.mapper;

import com.universite.module.domain.Module;
import com.universite.module.domain.Specialisation;
import com.universite.module.service.dto.ModuleDTO;
import com.universite.module.service.dto.SpecialisationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialisation} and its DTO {@link SpecialisationDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialisationMapper extends EntityMapper<SpecialisationDTO, Specialisation> {
    @Mapping(target = "idModule", source = "idModule", qualifiedByName = "moduleId")
    SpecialisationDTO toDto(Specialisation s);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);
}
