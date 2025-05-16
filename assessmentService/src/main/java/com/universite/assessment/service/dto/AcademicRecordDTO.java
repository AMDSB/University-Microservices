package com.universite.assessment.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.assessment.domain.AcademicRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AcademicRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idAcademicRecord;

    private Float moyenne;

    private String mention;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdAcademicRecord() {
        return idAcademicRecord;
    }

    public void setIdAcademicRecord(Integer idAcademicRecord) {
        this.idAcademicRecord = idAcademicRecord;
    }

    public Float getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Float moyenne) {
        this.moyenne = moyenne;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcademicRecordDTO)) {
            return false;
        }

        AcademicRecordDTO academicRecordDTO = (AcademicRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, academicRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcademicRecordDTO{" +
            "id=" + getId() +
            ", idAcademicRecord=" + getIdAcademicRecord() +
            ", moyenne=" + getMoyenne() +
            ", mention='" + getMention() + "'" +
            "}";
    }
}
