package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.TeachingAssignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeachingAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idTeachingAssignment;

    private Integer academicYear;

    private TeacherDTO idTeacher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdTeachingAssignment() {
        return idTeachingAssignment;
    }

    public void setIdTeachingAssignment(Integer idTeachingAssignment) {
        this.idTeachingAssignment = idTeachingAssignment;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public TeacherDTO getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(TeacherDTO idTeacher) {
        this.idTeacher = idTeacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeachingAssignmentDTO)) {
            return false;
        }

        TeachingAssignmentDTO teachingAssignmentDTO = (TeachingAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teachingAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeachingAssignmentDTO{" +
            "id=" + getId() +
            ", idTeachingAssignment=" + getIdTeachingAssignment() +
            ", academicYear=" + getAcademicYear() +
            ", idTeacher=" + getIdTeacher() +
            "}";
    }
}
