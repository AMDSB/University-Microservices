package com.universite.module.service.mapper;

import com.universite.module.domain.Module;
import com.universite.module.service.dto.ModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Module} and its DTO {@link ModuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModuleMapper extends EntityMapper<ModuleDTO, Module> {}
