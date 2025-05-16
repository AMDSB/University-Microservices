package com.universite.report.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportDetail.
 */
@Entity
@Table(name = "report_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "module_name", length = 100)
    private String moduleName;

    @Column(name = "number_of_students")
    private Integer numberOfStudents;

    @Column(name = "number_passed")
    private Integer numberPassed;

    @Column(name = "number_failed")
    private Integer numberFailed;

    @Column(name = "average_grade")
    private Float averageGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    private Report report;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public ReportDetail moduleName(String moduleName) {
        this.setModuleName(moduleName);
        return this;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getNumberOfStudents() {
        return this.numberOfStudents;
    }

    public ReportDetail numberOfStudents(Integer numberOfStudents) {
        this.setNumberOfStudents(numberOfStudents);
        return this;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public Integer getNumberPassed() {
        return this.numberPassed;
    }

    public ReportDetail numberPassed(Integer numberPassed) {
        this.setNumberPassed(numberPassed);
        return this;
    }

    public void setNumberPassed(Integer numberPassed) {
        this.numberPassed = numberPassed;
    }

    public Integer getNumberFailed() {
        return this.numberFailed;
    }

    public ReportDetail numberFailed(Integer numberFailed) {
        this.setNumberFailed(numberFailed);
        return this;
    }

    public void setNumberFailed(Integer numberFailed) {
        this.numberFailed = numberFailed;
    }

    public Float getAverageGrade() {
        return this.averageGrade;
    }

    public ReportDetail averageGrade(Float averageGrade) {
        this.setAverageGrade(averageGrade);
        return this;
    }

    public void setAverageGrade(Float averageGrade) {
        this.averageGrade = averageGrade;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ReportDetail report(Report report) {
        this.setReport(report);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportDetail{" +
            "id=" + getId() +
            ", moduleName='" + getModuleName() + "'" +
            ", numberOfStudents=" + getNumberOfStudents() +
            ", numberPassed=" + getNumberPassed() +
            ", numberFailed=" + getNumberFailed() +
            ", averageGrade=" + getAverageGrade() +
            "}";
    }
}
