package com.universite.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AcademicRecord.
 */
@Entity
@Table(name = "academic_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AcademicRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_academic_record", nullable = false)
    private Integer idAcademicRecord;

    @Column(name = "moyenne")
    private Float moyenne;

    @Column(name = "mention")
    private String mention;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AcademicRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdAcademicRecord() {
        return this.idAcademicRecord;
    }

    public AcademicRecord idAcademicRecord(Integer idAcademicRecord) {
        this.setIdAcademicRecord(idAcademicRecord);
        return this;
    }

    public void setIdAcademicRecord(Integer idAcademicRecord) {
        this.idAcademicRecord = idAcademicRecord;
    }

    public Float getMoyenne() {
        return this.moyenne;
    }

    public AcademicRecord moyenne(Float moyenne) {
        this.setMoyenne(moyenne);
        return this;
    }

    public void setMoyenne(Float moyenne) {
        this.moyenne = moyenne;
    }

    public String getMention() {
        return this.mention;
    }

    public AcademicRecord mention(String mention) {
        this.setMention(mention);
        return this;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AcademicRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((AcademicRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcademicRecord{" +
            "id=" + getId() +
            ", idAcademicRecord=" + getIdAcademicRecord() +
            ", moyenne=" + getMoyenne() +
            ", mention='" + getMention() + "'" +
            "}";
    }
}
