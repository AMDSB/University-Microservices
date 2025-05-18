package com.universite.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_report", nullable = false)
    private Integer idReport;

    @Column(name = "taux_reussite")
    private Double tauxReussite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdReport() {
        return this.idReport;
    }

    public Report idReport(Integer idReport) {
        this.setIdReport(idReport);
        return this;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    public Double getTauxReussite() {
        return this.tauxReussite;
    }

    public Report tauxReussite(Double tauxReussite) {
        this.setTauxReussite(tauxReussite);
        return this;
    }

    public void setTauxReussite(Double tauxReussite) {
        this.tauxReussite = tauxReussite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return getId() != null && getId().equals(((Report) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", idReport=" + getIdReport() +
            ", tauxReussite=" + getTauxReussite() +
            "}";
    }
}
