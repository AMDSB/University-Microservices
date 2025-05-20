package com.universite.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Disponibilite.
 */
@Entity
@Table(name = "disponibilite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Disponibilite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_disponibilite", nullable = false)
    private Integer idDisponibilite;

    @Column(name = "debut_disponibilite")
    private Instant debutDisponibilite;

    @Column(name = "fin_disponibilite")
    private Instant finDisponibilite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Disponibilite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdDisponibilite() {
        return this.idDisponibilite;
    }

    public Disponibilite idDisponibilite(Integer idDisponibilite) {
        this.setIdDisponibilite(idDisponibilite);
        return this;
    }

    public void setIdDisponibilite(Integer idDisponibilite) {
        this.idDisponibilite = idDisponibilite;
    }

    public Instant getDebutDisponibilite() {
        return this.debutDisponibilite;
    }

    public Disponibilite debutDisponibilite(Instant debutDisponibilite) {
        this.setDebutDisponibilite(debutDisponibilite);
        return this;
    }

    public void setDebutDisponibilite(Instant debutDisponibilite) {
        this.debutDisponibilite = debutDisponibilite;
    }

    public Instant getFinDisponibilite() {
        return this.finDisponibilite;
    }

    public Disponibilite finDisponibilite(Instant finDisponibilite) {
        this.setFinDisponibilite(finDisponibilite);
        return this;
    }

    public void setFinDisponibilite(Instant finDisponibilite) {
        this.finDisponibilite = finDisponibilite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disponibilite)) {
            return false;
        }
        return getId() != null && getId().equals(((Disponibilite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Disponibilite{" +
            "id=" + getId() +
            ", idDisponibilite=" + getIdDisponibilite() +
            ", debutDisponibilite='" + getDebutDisponibilite() + "'" +
            ", finDisponibilite='" + getFinDisponibilite() + "'" +
            "}";
    }
}
