package com.universite.teacher.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.teacher.domain.Teacher} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeacherDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idTeacher;

    private String grade;

    private String specialite;

    private DisponibiliteDTO idDisponibilite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(Integer idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public DisponibiliteDTO getIdDisponibilite() {
        return idDisponibilite;
    }

    public void setIdDisponibilite(DisponibiliteDTO idDisponibilite) {
        this.idDisponibilite = idDisponibilite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeacherDTO)) {
            return false;
        }

        TeacherDTO teacherDTO = (TeacherDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teacherDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherDTO{" +
            "id=" + getId() +
            ", idTeacher=" + getIdTeacher() +
            ", grade='" + getGrade() + "'" +
            ", specialite='" + getSpecialite() + "'" +
            ", idDisponibilite=" + getIdDisponibilite() +
            "}";
    }
}
