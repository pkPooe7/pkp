package com.pkp.web.rest;

import com.pkp.domain.World;
import com.pkp.repository.WorldRepository;
import com.pkp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.pkp.domain.World}.
 */
@RestController
@RequestMapping("/api")
public class WorldResource {

    private final Logger log = LoggerFactory.getLogger(WorldResource.class);

    private static final String ENTITY_NAME = "world";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorldRepository worldRepository;

    public WorldResource(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    /**
     * {@code POST  /worlds} : Create a new world.
     *
     * @param world the world to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new world, or with status {@code 400 (Bad Request)} if the world has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/worlds")
    public ResponseEntity<World> createWorld(@Valid @RequestBody World world) throws URISyntaxException {
        log.debug("REST request to save World : {}", world);
        if (world.getId() != null) {
            throw new BadRequestAlertException("A new world cannot already have an ID", ENTITY_NAME, "idexists");
        }
        World result = worldRepository.save(world);
        return ResponseEntity.created(new URI("/api/worlds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /worlds} : Updates an existing world.
     *
     * @param world the world to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated world,
     * or with status {@code 400 (Bad Request)} if the world is not valid,
     * or with status {@code 500 (Internal Server Error)} if the world couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/worlds")
    public ResponseEntity<World> updateWorld(@Valid @RequestBody World world) throws URISyntaxException {
        log.debug("REST request to update World : {}", world);
        if (world.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        World result = worldRepository.save(world);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, world.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /worlds} : get all the worlds.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of worlds in body.
     */
    @GetMapping("/worlds")
    public List<World> getAllWorlds() {
        log.debug("REST request to get all Worlds");
        return worldRepository.findAll();
    }

    /**
     * {@code GET  /worlds/:id} : get the "id" world.
     *
     * @param id the id of the world to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the world, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/worlds/{id}")
    public ResponseEntity<World> getWorld(@PathVariable Long id) {
        log.debug("REST request to get World : {}", id);
        Optional<World> world = worldRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(world);
    }

    /**
     * {@code DELETE  /worlds/:id} : delete the "id" world.
     *
     * @param id the id of the world to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/worlds/{id}")
    public ResponseEntity<Void> deleteWorld(@PathVariable Long id) {
        log.debug("REST request to delete World : {}", id);
        worldRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
