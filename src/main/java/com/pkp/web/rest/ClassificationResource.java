package com.pkp.web.rest;

import com.pkp.domain.Classification;
import com.pkp.repository.ClassificationRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.pkp.domain.Classification}.
 */
@RestController
@RequestMapping("/api")
public class ClassificationResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationResource.class);

    private static final String ENTITY_NAME = "classification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationRepository classificationRepository;

    public ClassificationResource(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    /**
     * {@code POST  /classifications} : Create a new classification.
     *
     * @param classification the classification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classification, or with status {@code 400 (Bad Request)} if the classification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classifications")
    public ResponseEntity<Classification> createClassification(@Valid @RequestBody Classification classification) throws URISyntaxException {
        log.debug("REST request to save Classification : {}", classification);
        if (classification.getId() != null) {
            throw new BadRequestAlertException("A new classification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Classification result = classificationRepository.save(classification);
        return ResponseEntity.created(new URI("/api/classifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classifications} : Updates an existing classification.
     *
     * @param classification the classification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classification,
     * or with status {@code 400 (Bad Request)} if the classification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classifications")
    public ResponseEntity<Classification> updateClassification(@Valid @RequestBody Classification classification) throws URISyntaxException {
        log.debug("REST request to update Classification : {}", classification);
        if (classification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Classification result = classificationRepository.save(classification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classification.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /classifications} : get all the classifications.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifications in body.
     */
    @GetMapping("/classifications")
    public List<Classification> getAllClassifications(@RequestParam(required = false) String filter) {
        if ("type-is-null".equals(filter)) {
            log.debug("REST request to get all Classifications where type is null");
            return StreamSupport
                .stream(classificationRepository.findAll().spliterator(), false)
                .filter(classification -> classification.getType() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Classifications");
        return classificationRepository.findAll();
    }

    /**
     * {@code GET  /classifications/:id} : get the "id" classification.
     *
     * @param id the id of the classification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classifications/{id}")
    public ResponseEntity<Classification> getClassification(@PathVariable Long id) {
        log.debug("REST request to get Classification : {}", id);
        Optional<Classification> classification = classificationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(classification);
    }

    /**
     * {@code DELETE  /classifications/:id} : delete the "id" classification.
     *
     * @param id the id of the classification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classifications/{id}")
    public ResponseEntity<Void> deleteClassification(@PathVariable Long id) {
        log.debug("REST request to delete Classification : {}", id);
        classificationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
