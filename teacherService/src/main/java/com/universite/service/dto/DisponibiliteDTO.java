package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.Disponibilite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DisponibiliteDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idDisponibilite;

    private Instant debutDisponibilite;

    private Instant finDisponibilite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdDisponibilite() {
        return idDisponibilite;
    }

    public void setIdDisponibilite(Integer idDisponibilite) {
        this.idDisponibilite = idDisponibilite;
    }

    public Instant getDebutDisponibilite() {
        return debutDisponibilite;
    }

    public void setDebutDisponibilite(Instant debutDisponibilite) {
        this.debutDisponibilite = debutDisponibilite;
    }

    public Instant getFinDisponibilite() {
        return finDisponibilite;
    }

    public void setFinDisponibilite(Instant finDisponibilite) {
        this.finDisponibilite = finDisponibilite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisponibiliteDTO)) {
            return false;
        }

        DisponibiliteDTO disponibiliteDTO = (DisponibiliteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, disponibiliteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisponibiliteDTO{" +
            "id=" + getId() +
            ", idDisponibilite=" + getIdDisponibilite() +
            ", debutDisponibilite='" + getDebutDisponibilite() + "'" +
            ", finDisponibilite='" + getFinDisponibilite() + "'" +
            "}";
    }
}
