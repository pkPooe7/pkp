package com.pkp.web.rest;

import com.pkp.domain.Alien;
import com.pkp.repository.AlienRepository;
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
 * REST controller for managing {@link com.pkp.domain.Alien}.
 */
@RestController
@RequestMapping("/api")
public class AlienResource {

    private final Logger log = LoggerFactory.getLogger(AlienResource.class);

    private static final String ENTITY_NAME = "alien";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlienRepository alienRepository;

    public AlienResource(AlienRepository alienRepository) {
        this.alienRepository = alienRepository;
    }

    /**
     * {@code POST  /aliens} : Create a new alien.
     *
     * @param alien the alien to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alien, or with status {@code 400 (Bad Request)} if the alien has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aliens")
    public ResponseEntity<Alien> createAlien(@Valid @RequestBody Alien alien) throws URISyntaxException {
        log.debug("REST request to save Alien : {}", alien);
        if (alien.getId() != null) {
            throw new BadRequestAlertException("A new alien cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Alien result = alienRepository.save(alien);
        return ResponseEntity.created(new URI("/api/aliens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aliens} : Updates an existing alien.
     *
     * @param alien the alien to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alien,
     * or with status {@code 400 (Bad Request)} if the alien is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alien couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aliens")
    public ResponseEntity<Alien> updateAlien(@Valid @RequestBody Alien alien) throws URISyntaxException {
        log.debug("REST request to update Alien : {}", alien);
        if (alien.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Alien result = alienRepository.save(alien);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alien.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /aliens} : get all the aliens.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aliens in body.
     */
    @GetMapping("/aliens")
    public List<Alien> getAllAliens() {
        log.debug("REST request to get all Aliens");
        return alienRepository.findAll();
    }

    /**
     * {@code GET  /aliens/:id} : get the "id" alien.
     *
     * @param id the id of the alien to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alien, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aliens/{id}")
    public ResponseEntity<Alien> getAlien(@PathVariable Long id) {
        log.debug("REST request to get Alien : {}", id);
        Optional<Alien> alien = alienRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(alien);
    }

    /**
     * {@code DELETE  /aliens/:id} : delete the "id" alien.
     *
     * @param id the id of the alien to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aliens/{id}")
    public ResponseEntity<Void> deleteAlien(@PathVariable Long id) {
        log.debug("REST request to delete Alien : {}", id);
        alienRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
