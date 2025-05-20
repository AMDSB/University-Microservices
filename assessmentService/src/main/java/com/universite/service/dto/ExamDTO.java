package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.universite.domain.Exam} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer idExam;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdExam() {
        return idExam;
    }

    public void setIdExam(Integer idExam) {
        this.idExam = idExam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamDTO)) {
            return false;
        }

        ExamDTO examDTO = (ExamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamDTO{" +
            "id=" + getId() +
            ", idExam=" + getIdExam() +
            ", type='" + getType() + "'" +
            "}";
    }
}
