package com.pkp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Alien.
 */
@Entity
@Table(name = "alien")
public class Alien implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(name = "species", length = 50, nullable = false)
    private String species;

    @NotNull
    @Size(max = 50)
    @Column(name = "planet", length = 50, nullable = false)
    private String planet;

    @OneToOne
    @JoinColumn(unique = true)
    private Classification catergory;

    @ManyToOne
    @JsonIgnoreProperties("alienWorlds")
    private World homeWorld;

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

    public Alien name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public Alien species(String species) {
        this.species = species;
        return this;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getPlanet() {
        return planet;
    }

    public Alien planet(String planet) {
        this.planet = planet;
        return this;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public Classification getCatergory() {
        return catergory;
    }

    public Alien catergory(Classification classification) {
        this.catergory = classification;
        return this;
    }

    public void setCatergory(Classification classification) {
        this.catergory = classification;
    }

    public World getHomeWorld() {
        return homeWorld;
    }

    public Alien homeWorld(World world) {
        this.homeWorld = world;
        return this;
    }

    public void setHomeWorld(World world) {
        this.homeWorld = world;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alien)) {
            return false;
        }
        return id != null && id.equals(((Alien) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Alien{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", species='" + getSpecies() + "'" +
            ", planet='" + getPlanet() + "'" +
            "}";
    }
}
