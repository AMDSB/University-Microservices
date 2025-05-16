package com.universite.module.service.mapper;

import com.universite.module.domain.Departement;
import com.universite.module.domain.Filiere;
import com.universite.module.domain.Module;
import com.universite.module.domain.Specialisation;
import com.universite.module.service.dto.DepartementDTO;
import com.universite.module.service.dto.FiliereDTO;
import com.universite.module.service.dto.ModuleDTO;
import com.universite.module.service.dto.SpecialisationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Filiere} and its DTO {@link FiliereDTO}.
 */
@Mapper(componentModel = "spring")
public interface FiliereMapper extends EntityMapper<FiliereDTO, Filiere> {
    @Mapping(target = "idModule", source = "idModule", qualifiedByName = "moduleId")
    @Mapping(target = "idDepartement", source = "idDepartement", qualifiedByName = "departementId")
    @Mapping(target = "idSpecialisation", source = "idSpecialisation", qualifiedByName = "specialisationId")
    FiliereDTO toDto(Filiere s);

    @Named("moduleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModuleDTO toDtoModuleId(Module module);

    @Named("departementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartementDTO toDtoDepartementId(Departement departement);

    @Named("specialisationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SpecialisationDTO toDtoSpecialisationId(Specialisation specialisation);
}
