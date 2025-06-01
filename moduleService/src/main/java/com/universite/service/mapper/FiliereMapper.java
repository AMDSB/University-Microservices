package com.universite.service.mapper;

import com.universite.domain.Departement;
import com.universite.domain.Filiere;
import com.universite.domain.Module;
import com.universite.domain.Specialisation;
import com.universite.service.dto.DepartementDTO;
import com.universite.service.dto.FiliereDTO;
import com.universite.service.dto.ModuleDTO;
import com.universite.service.dto.SpecialisationDTO;
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
