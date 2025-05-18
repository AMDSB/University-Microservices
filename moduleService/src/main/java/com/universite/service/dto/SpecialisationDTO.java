package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.Specialisation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpecialisationDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idSpecialisation;

    private String name;

    private ModuleDTO idModule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdSpecialisation() {
        return idSpecialisation;
    }

    public void setIdSpecialisation(Integer idSpecialisation) {
        this.idSpecialisation = idSpecialisation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModuleDTO getIdModule() {
        return idModule;
    }

    public void setIdModule(ModuleDTO idModule) {
        this.idModule = idModule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialisationDTO)) {
            return false;
        }

        SpecialisationDTO specialisationDTO = (SpecialisationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, specialisationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpecialisationDTO{" +
            "id=" + getId() +
            ", idSpecialisation=" + getIdSpecialisation() +
            ", name='" + getName() + "'" +
            ", idModule=" + getIdModule() +
            "}";
    }
}
