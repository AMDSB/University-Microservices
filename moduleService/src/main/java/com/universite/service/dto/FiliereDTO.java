package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.Filiere} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FiliereDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idFiliere;

    private String name;

    private String description;

    private ModuleDTO idModule;

    private DepartementDTO idDepartement;

    private SpecialisationDTO idSpecialisation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdFiliere() {
        return idFiliere;
    }

    public void setIdFiliere(Integer idFiliere) {
        this.idFiliere = idFiliere;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModuleDTO getIdModule() {
        return idModule;
    }

    public void setIdModule(ModuleDTO idModule) {
        this.idModule = idModule;
    }

    public DepartementDTO getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(DepartementDTO idDepartement) {
        this.idDepartement = idDepartement;
    }

    public SpecialisationDTO getIdSpecialisation() {
        return idSpecialisation;
    }

    public void setIdSpecialisation(SpecialisationDTO idSpecialisation) {
        this.idSpecialisation = idSpecialisation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiliereDTO)) {
            return false;
        }

        FiliereDTO filiereDTO = (FiliereDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filiereDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiliereDTO{" +
            "id=" + getId() +
            ", idFiliere=" + getIdFiliere() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", idModule=" + getIdModule() +
            ", idDepartement=" + getIdDepartement() +
            ", idSpecialisation=" + getIdSpecialisation() +
            "}";
    }
}
