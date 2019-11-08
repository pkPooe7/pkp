package com.pkp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Technology.
 */
@Entity
@Table(name = "technology")
public class Technology implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "aquired", nullable = false)
    private Boolean aquired;

    @NotNull
    @Size(max = 50)
    @Column(name = "speci", length = 50, nullable = false)
    private String speci;

    @ManyToMany
    @JoinTable(name = "technology_species_tech",
               joinColumns = @JoinColumn(name = "technology_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "species_tech_id", referencedColumnName = "id"))
    private Set<Classification> speciesTeches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Technology name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAquired() {
        return aquired;
    }

    public Technology aquired(Boolean aquired) {
        this.aquired = aquired;
        return this;
    }

    public void setAquired(Boolean aquired) {
        this.aquired = aquired;
    }

    public String getSpeci() {
        return speci;
    }

    public Technology speci(String speci) {
        this.speci = speci;
        return this;
    }

    public void setSpeci(String speci) {
        this.speci = speci;
    }

    public Set<Classification> getSpeciesTeches() {
        return speciesTeches;
    }

    public Technology speciesTeches(Set<Classification> classifications) {
        this.speciesTeches = classifications;
        return this;
    }

    public Technology addSpeciesTech(Classification classification) {
        this.speciesTeches.add(classification);
        classification.getRaceNames().add(this);
        return this;
    }

    public Technology removeSpeciesTech(Classification classification) {
        this.speciesTeches.remove(classification);
        classification.getRaceNames().remove(this);
        return this;
    }

    public void setSpeciesTeches(Set<Classification> classifications) {
        this.speciesTeches = classifications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Technology)) {
            return false;
        }
        return id != null && id.equals(((Technology) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Technology{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", aquired='" + isAquired() + "'" +
            ", speci='" + getSpeci() + "'" +
            "}";
    }
}
