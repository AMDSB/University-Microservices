package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.Edt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EdtDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idEdt;

    private String duration;

    private FiliereDTO idFiliere;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdEdt() {
        return idEdt;
    }

    public void setIdEdt(Integer idEdt) {
        this.idEdt = idEdt;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public FiliereDTO getIdFiliere() {
        return idFiliere;
    }

    public void setIdFiliere(FiliereDTO idFiliere) {
        this.idFiliere = idFiliere;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EdtDTO)) {
            return false;
        }

        EdtDTO edtDTO = (EdtDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, edtDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EdtDTO{" +
            "id=" + getId() +
            ", idEdt=" + getIdEdt() +
            ", duration='" + getDuration() + "'" +
            ", idFiliere=" + getIdFiliere() +
            "}";
    }
}
