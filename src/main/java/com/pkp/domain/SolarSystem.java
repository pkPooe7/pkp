package com.pkp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A SolarSystem.
 */
@Entity
@Table(name = "solar_system")
public class SolarSystem implements Serializable {

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
    @Column(name = "galaxy", length = 50, nullable = false)
    private String galaxy;

    @OneToMany(mappedBy = "homeSystem")
    private Set<World> worldOrigins = new HashSet<>();

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

    public SolarSystem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGalaxy() {
        return galaxy;
    }

    public SolarSystem galaxy(String galaxy) {
        this.galaxy = galaxy;
        return this;
    }

    public void setGalaxy(String galaxy) {
        this.galaxy = galaxy;
    }

    public Set<World> getWorldOrigins() {
        return worldOrigins;
    }

    public SolarSystem worldOrigins(Set<World> worlds) {
        this.worldOrigins = worlds;
        return this;
    }

    public SolarSystem addWorldOrigin(World world) {
        this.worldOrigins.add(world);
        world.setHomeSystem(this);
        return this;
    }

    public SolarSystem removeWorldOrigin(World world) {
        this.worldOrigins.remove(world);
        world.setHomeSystem(null);
        return this;
    }

    public void setWorldOrigins(Set<World> worlds) {
        this.worldOrigins = worlds;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolarSystem)) {
            return false;
        }
        return id != null && id.equals(((SolarSystem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SolarSystem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", galaxy='" + getGalaxy() + "'" +
            "}";
    }
}
