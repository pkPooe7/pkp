package com.pkp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.pkp.domain.enumeration.Hands;

/**
 * A Classification.
 */
@Entity
@Table(name = "classification")
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "handed", nullable = false)
    private Hands handed;

    @OneToOne(mappedBy = "catergory")
    @JsonIgnore
    private Alien type;

    @ManyToMany(mappedBy = "speciesTeches")
    @JsonIgnore
    private Set<Technology> raceNames = new HashSet<>();

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

    public Classification name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hands getHanded() {
        return handed;
    }

    public Classification handed(Hands handed) {
        this.handed = handed;
        return this;
    }

    public void setHanded(Hands handed) {
        this.handed = handed;
    }

    public Alien getType() {
        return type;
    }

    public Classification type(Alien alien) {
        this.type = alien;
        return this;
    }

    public void setType(Alien alien) {
        this.type = alien;
    }

    public Set<Technology> getRaceNames() {
        return raceNames;
    }

    public Classification raceNames(Set<Technology> technologies) {
        this.raceNames = technologies;
        return this;
    }

    public Classification addRaceName(Technology technology) {
        this.raceNames.add(technology);
        technology.getSpeciesTeches().add(this);
        return this;
    }

    public Classification removeRaceName(Technology technology) {
        this.raceNames.remove(technology);
        technology.getSpeciesTeches().remove(this);
        return this;
    }

    public void setRaceNames(Set<Technology> technologies) {
        this.raceNames = technologies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classification)) {
            return false;
        }
        return id != null && id.equals(((Classification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Classification{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", handed='" + getHanded() + "'" +
            "}";
    }
}
