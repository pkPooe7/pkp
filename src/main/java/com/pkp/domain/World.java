package com.pkp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A World.
 */
@Entity
@Table(name = "world")
public class World implements Serializable {

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
    @Column(name = "jhi_system", length = 50, nullable = false)
    private String system;

    @OneToMany(mappedBy = "homeWorld")
    private Set<Alien> alienWorlds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("worldOrigins")
    private SolarSystem homeSystem;

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

    public World name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystem() {
        return system;
    }

    public World system(String system) {
        this.system = system;
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Set<Alien> getAlienWorlds() {
        return alienWorlds;
    }

    public World alienWorlds(Set<Alien> aliens) {
        this.alienWorlds = aliens;
        return this;
    }

    public World addAlienWorld(Alien alien) {
        this.alienWorlds.add(alien);
        alien.setHomeWorld(this);
        return this;
    }

    public World removeAlienWorld(Alien alien) {
        this.alienWorlds.remove(alien);
        alien.setHomeWorld(null);
        return this;
    }

    public void setAlienWorlds(Set<Alien> aliens) {
        this.alienWorlds = aliens;
    }

    public SolarSystem getHomeSystem() {
        return homeSystem;
    }

    public World homeSystem(SolarSystem solarSystem) {
        this.homeSystem = solarSystem;
        return this;
    }

    public void setHomeSystem(SolarSystem solarSystem) {
        this.homeSystem = solarSystem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof World)) {
            return false;
        }
        return id != null && id.equals(((World) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "World{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", system='" + getSystem() + "'" +
            "}";
    }
}
