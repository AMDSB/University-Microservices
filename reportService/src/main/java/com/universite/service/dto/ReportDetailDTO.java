package com.universite.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the {@link com.universite.domain.ReportDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDetailDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String moduleName;

    private Integer numberOfStudents;

    private Integer numberPassed;

    private Integer numberFailed;

    private Float averageGrade;

    private ReportDTO report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public Integer getNumberPassed() {
        return numberPassed;
    }

    public void setNumberPassed(Integer numberPassed) {
        this.numberPassed = numberPassed;
    }

    public Integer getNumberFailed() {
        return numberFailed;
    }

    public void setNumberFailed(Integer numberFailed) {
        this.numberFailed = numberFailed;
    }

    public Float getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Float averageGrade) {
        this.averageGrade = averageGrade;
    }

    public ReportDTO getReport() {
        return report;
    }

    public void setReport(ReportDTO report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDetailDTO)) {
            return false;
        }

        ReportDetailDTO reportDetailDTO = (ReportDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDetailDTO{" +
            "id=" + getId() +
            ", moduleName='" + getModuleName() + "'" +
            ", numberOfStudents=" + getNumberOfStudents() +
            ", numberPassed=" + getNumberPassed() +
            ", numberFailed=" + getNumberFailed() +
            ", averageGrade=" + getAverageGrade() +
            ", report=" + getReport() +
            "}";
    }
}
