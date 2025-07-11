package com.universite.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Specialisation.
 */
@Entity
@Table(name = "specialisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Specialisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_specialisation", nullable = false)
    private Integer idSpecialisation;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Module idModule;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Specialisation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdSpecialisation() {
        return this.idSpecialisation;
    }

    public Specialisation idSpecialisation(Integer idSpecialisation) {
        this.setIdSpecialisation(idSpecialisation);
        return this;
    }

    public void setIdSpecialisation(Integer idSpecialisation) {
        this.idSpecialisation = idSpecialisation;
    }

    public String getName() {
        return this.name;
    }

    public Specialisation name(String name) {
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

    public Specialisation idModule(Module module) {
        this.setIdModule(module);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specialisation)) {
            return false;
        }
        return getId() != null && getId().equals(((Specialisation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specialisation{" +
            "id=" + getId() +
            ", idSpecialisation=" + getIdSpecialisation() +
            ", name='" + getName() + "'" +
            "}";
    }
}
