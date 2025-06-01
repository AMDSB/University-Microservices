package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.Departement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartementDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idDepartement;

    private String name;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(Integer idDepartement) {
        this.idDepartement = idDepartement;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartementDTO)) {
            return false;
        }

        DepartementDTO departementDTO = (DepartementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementDTO{" +
            "id=" + getId() +
            ", idDepartement=" + getIdDepartement() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
