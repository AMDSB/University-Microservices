package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.Module} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModuleDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idModule;

    private Integer credit;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdModule() {
        return idModule;
    }

    public void setIdModule(Integer idModule) {
        this.idModule = idModule;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModuleDTO)) {
            return false;
        }

        ModuleDTO moduleDTO = (ModuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModuleDTO{" +
            "id=" + getId() +
            ", idModule=" + getIdModule() +
            ", credit=" + getCredit() +
            ", name='" + getName() + "'" +
            "}";
    }
}
