package com.universite.service.mapper;

import com.universite.domain.Module;
import com.universite.domain.Specialisation;
import com.universite.service.dto.ModuleDTO;
import com.universite.service.dto.SpecialisationDTO;
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
