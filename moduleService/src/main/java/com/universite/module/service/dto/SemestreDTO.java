package com.universite.module.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.module.domain.Semestre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SemestreDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idSemestre;

    private LocalDate startedAt;

    private LocalDate endedAt;

    private String name;

    private ModuleDTO idModule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdSemestre() {
        return idSemestre;
    }

    public void setIdSemestre(Integer idSemestre) {
        this.idSemestre = idSemestre;
    }

    public LocalDate getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDate getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDate endedAt) {
        this.endedAt = endedAt;
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
        if (!(o instanceof SemestreDTO)) {
            return false;
        }

        SemestreDTO semestreDTO = (SemestreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, semestreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SemestreDTO{" +
            "id=" + getId() +
            ", idSemestre=" + getIdSemestre() +
            ", startedAt='" + getStartedAt() + "'" +
            ", endedAt='" + getEndedAt() + "'" +
            ", name='" + getName() + "'" +
            ", idModule=" + getIdModule() +
            "}";
    }
}
