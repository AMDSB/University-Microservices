package com.universite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TeachingAssignment.
 */
@Entity
@Table(name = "teaching_assignment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeachingAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_teaching_assignment", nullable = false)
    private Integer idTeachingAssignment;

    @Column(name = "academic_year")
    private Integer academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "idDisponibilite" }, allowSetters = true)
    private Teacher idTeacher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TeachingAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdTeachingAssignment() {
        return this.idTeachingAssignment;
    }

    public TeachingAssignment idTeachingAssignment(Integer idTeachingAssignment) {
        this.setIdTeachingAssignment(idTeachingAssignment);
        return this;
    }

    public void setIdTeachingAssignment(Integer idTeachingAssignment) {
        this.idTeachingAssignment = idTeachingAssignment;
    }

    public Integer getAcademicYear() {
        return this.academicYear;
    }

    public TeachingAssignment academicYear(Integer academicYear) {
        this.setAcademicYear(academicYear);
        return this;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public Teacher getIdTeacher() {
        return this.idTeacher;
    }

    public void setIdTeacher(Teacher teacher) {
        this.idTeacher = teacher;
    }

    public TeachingAssignment idTeacher(Teacher teacher) {
        this.setIdTeacher(teacher);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeachingAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((TeachingAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeachingAssignment{" +
            "id=" + getId() +
            ", idTeachingAssignment=" + getIdTeachingAssignment() +
            ", academicYear=" + getAcademicYear() +
            "}";
    }
}
