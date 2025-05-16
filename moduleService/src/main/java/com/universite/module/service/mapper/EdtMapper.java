package com.universite.module.service.mapper;

import com.universite.module.domain.Edt;
import com.universite.module.domain.Filiere;
import com.universite.module.service.dto.EdtDTO;
import com.universite.module.service.dto.FiliereDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Edt} and its DTO {@link EdtDTO}.
 */
@Mapper(componentModel = "spring")
public interface EdtMapper extends EntityMapper<EdtDTO, Edt> {
    @Mapping(target = "idFiliere", source = "idFiliere", qualifiedByName = "filiereId")
    EdtDTO toDto(Edt s);

    @Named("filiereId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FiliereDTO toDtoFiliereId(Filiere filiere);
}
