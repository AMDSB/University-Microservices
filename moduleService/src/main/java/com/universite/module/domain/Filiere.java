package com.universite.module.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Filiere.
 */
@Entity
@Table(name = "filiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Filiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_filiere", nullable = false)
    private Integer idFiliere;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Module idModule;

    @ManyToOne(fetch = FetchType.LAZY)
    private Departement idDepartement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "idModule" }, allowSetters = true)
    private Specialisation idSpecialisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Filiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdFiliere() {
        return this.idFiliere;
    }

    public Filiere idFiliere(Integer idFiliere) {
        this.setIdFiliere(idFiliere);
        return this;
    }

    public void setIdFiliere(Integer idFiliere) {
        this.idFiliere = idFiliere;
    }

    public String getName() {
        return this.name;
    }

    public Filiere name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Filiere description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Module getIdModule() {
        return this.idModule;
    }

    public void setIdModule(Module module) {
        this.idModule = module;
    }

    public Filiere idModule(Module module) {
        this.setIdModule(module);
        return this;
    }

    public Departement getIdDepartement() {
        return this.idDepartement;
    }

    public void setIdDepartement(Departement departement) {
        this.idDepartement = departement;
    }

    public Filiere idDepartement(Departement departement) {
        this.setIdDepartement(departement);
        return this;
    }

    public Specialisation getIdSpecialisation() {
        return this.idSpecialisation;
    }

    public void setIdSpecialisation(Specialisation specialisation) {
        this.idSpecialisation = specialisation;
    }

    public Filiere idSpecialisation(Specialisation specialisation) {
        this.setIdSpecialisation(specialisation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Filiere)) {
            return false;
        }
        return getId() != null && getId().equals(((Filiere) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Filiere{" +
            "id=" + getId() +
            ", idFiliere=" + getIdFiliere() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
