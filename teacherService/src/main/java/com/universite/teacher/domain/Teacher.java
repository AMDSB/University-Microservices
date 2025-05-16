package com.universite.teacher.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Teacher.
 */
@Entity
@Table(name = "teacher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_teacher", nullable = false)
    private Integer idTeacher;

    @Column(name = "grade")
    private String grade;

    @Column(name = "specialite")
    private String specialite;

    @ManyToOne(fetch = FetchType.LAZY)
    private Disponibilite idDisponibilite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Teacher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdTeacher() {
        return this.idTeacher;
    }

    public Teacher idTeacher(Integer idTeacher) {
        this.setIdTeacher(idTeacher);
        return this;
    }

    public void setIdTeacher(Integer idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getGrade() {
        return this.grade;
    }

    public Teacher grade(String grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSpecialite() {
        return this.specialite;
    }

    public Teacher specialite(String specialite) {
        this.setSpecialite(specialite);
        return this;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public Disponibilite getIdDisponibilite() {
        return this.idDisponibilite;
    }

    public void setIdDisponibilite(Disponibilite disponibilite) {
        this.idDisponibilite = disponibilite;
    }

    public Teacher idDisponibilite(Disponibilite disponibilite) {
        this.setIdDisponibilite(disponibilite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Teacher)) {
            return false;
        }
        return getId() != null && getId().equals(((Teacher) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Teacher{" +
            "id=" + getId() +
            ", idTeacher=" + getIdTeacher() +
            ", grade='" + getGrade() + "'" +
            ", specialite='" + getSpecialite() + "'" +
            "}";
    }
}
