package com.pkp.web.rest;

import com.pkp.domain.SolarSystem;
import com.pkp.repository.SolarSystemRepository;
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
 * REST controller for managing {@link com.pkp.domain.SolarSystem}.
 */
@RestController
@RequestMapping("/api")
public class SolarSystemResource {

    private final Logger log = LoggerFactory.getLogger(SolarSystemResource.class);

    private static final String ENTITY_NAME = "solarSystem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolarSystemRepository solarSystemRepository;

    public SolarSystemResource(SolarSystemRepository solarSystemRepository) {
        this.solarSystemRepository = solarSystemRepository;
    }

    /**
     * {@code POST  /solar-systems} : Create a new solarSystem.
     *
     * @param solarSystem the solarSystem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solarSystem, or with status {@code 400 (Bad Request)} if the solarSystem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/solar-systems")
    public ResponseEntity<SolarSystem> createSolarSystem(@Valid @RequestBody SolarSystem solarSystem) throws URISyntaxException {
        log.debug("REST request to save SolarSystem : {}", solarSystem);
        if (solarSystem.getId() != null) {
            throw new BadRequestAlertException("A new solarSystem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolarSystem result = solarSystemRepository.save(solarSystem);
        return ResponseEntity.created(new URI("/api/solar-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /solar-systems} : Updates an existing solarSystem.
     *
     * @param solarSystem the solarSystem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solarSystem,
     * or with status {@code 400 (Bad Request)} if the solarSystem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solarSystem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/solar-systems")
    public ResponseEntity<SolarSystem> updateSolarSystem(@Valid @RequestBody SolarSystem solarSystem) throws URISyntaxException {
        log.debug("REST request to update SolarSystem : {}", solarSystem);
        if (solarSystem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SolarSystem result = solarSystemRepository.save(solarSystem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, solarSystem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /solar-systems} : get all the solarSystems.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of solarSystems in body.
     */
    @GetMapping("/solar-systems")
    public List<SolarSystem> getAllSolarSystems() {
        log.debug("REST request to get all SolarSystems");
        return solarSystemRepository.findAll();
    }

    /**
     * {@code GET  /solar-systems/:id} : get the "id" solarSystem.
     *
     * @param id the id of the solarSystem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solarSystem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/solar-systems/{id}")
    public ResponseEntity<SolarSystem> getSolarSystem(@PathVariable Long id) {
        log.debug("REST request to get SolarSystem : {}", id);
        Optional<SolarSystem> solarSystem = solarSystemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(solarSystem);
    }

    /**
     * {@code DELETE  /solar-systems/:id} : delete the "id" solarSystem.
     *
     * @param id the id of the solarSystem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/solar-systems/{id}")
    public ResponseEntity<Void> deleteSolarSystem(@PathVariable Long id) {
        log.debug("REST request to delete SolarSystem : {}", id);
        solarSystemRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
