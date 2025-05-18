package com.universite.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Semestre.
 */
@Entity
@Table(name = "semestre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Semestre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_semestre", nullable = false)
    private Integer idSemestre;

    @Column(name = "started_at")
    private LocalDate startedAt;

    @Column(name = "ended_at")
    private LocalDate endedAt;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Module idModule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Semestre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdSemestre() {
        return this.idSemestre;
    }

    public Semestre idSemestre(Integer idSemestre) {
        this.setIdSemestre(idSemestre);
        return this;
    }

    public void setIdSemestre(Integer idSemestre) {
        this.idSemestre = idSemestre;
    }

    public LocalDate getStartedAt() {
        return this.startedAt;
    }

    public Semestre startedAt(LocalDate startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDate getEndedAt() {
        return this.endedAt;
    }

    public Semestre endedAt(LocalDate endedAt) {
        this.setEndedAt(endedAt);
        return this;
    }

    public void setEndedAt(LocalDate endedAt) {
        this.endedAt = endedAt;
    }

    public String getName() {
        return this.name;
    }

    public Semestre name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Module getIdModule() {
        return this.idModule;
    }

    public void setIdModule(Module module) {
        this.idModule = module;
    }

    public Semestre idModule(Module module) {
        this.setIdModule(module);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semestre)) {
            return false;
        }
        return getId() != null && getId().equals(((Semestre) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semestre{" +
            "id=" + getId() +
            ", idSemestre=" + getIdSemestre() +
            ", startedAt='" + getStartedAt() + "'" +
            ", endedAt='" + getEndedAt() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
