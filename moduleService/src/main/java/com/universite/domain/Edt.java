package com.universite.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Edt.
 */
@Entity
@Table(name = "edt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Edt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_edt", nullable = false)
    private Integer idEdt;

    @Column(name = "duration")
    private String duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "idModule", "idDepartement", "idSpecialisation" }, allowSetters = true)
    private Filiere idFiliere;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Edt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdEdt() {
        return this.idEdt;
    }

    public Edt idEdt(Integer idEdt) {
        this.setIdEdt(idEdt);
        return this;
    }

    public void setIdEdt(Integer idEdt) {
        this.idEdt = idEdt;
    }

    public String getDuration() {
        return this.duration;
    }

    public Edt duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Filiere getIdFiliere() {
        return this.idFiliere;
    }

    public void setIdFiliere(Filiere filiere) {
        this.idFiliere = filiere;
    }

    public Edt idFiliere(Filiere filiere) {
        this.setIdFiliere(filiere);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edt)) {
            return false;
        }
        return getId() != null && getId().equals(((Edt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Edt{" +
            "id=" + getId() +
            ", idEdt=" + getIdEdt() +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
