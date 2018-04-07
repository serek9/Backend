package com.proyecto.serflix.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proyecto.serflix.domain.SerieRecomendation;

import com.proyecto.serflix.repository.SerieRecomendationRepository;
import com.proyecto.serflix.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SerieRecomendation.
 */
@RestController
@RequestMapping("/api")
public class SerieRecomendationResource {

    private final Logger log = LoggerFactory.getLogger(SerieRecomendationResource.class);
        
    @Inject
    private SerieRecomendationRepository serieRecomendationRepository;

    /**
     * POST  /serie-recomendations : Create a new serieRecomendation.
     *
     * @param serieRecomendation the serieRecomendation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serieRecomendation, or with status 400 (Bad Request) if the serieRecomendation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/serie-recomendations")
    @Timed
    public ResponseEntity<SerieRecomendation> createSerieRecomendation(@RequestBody SerieRecomendation serieRecomendation) throws URISyntaxException {
        log.debug("REST request to save SerieRecomendation : {}", serieRecomendation);
        if (serieRecomendation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serieRecomendation", "idexists", "A new serieRecomendation cannot already have an ID")).body(null);
        }
        SerieRecomendation result = serieRecomendationRepository.save(serieRecomendation);
        return ResponseEntity.created(new URI("/api/serie-recomendations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serieRecomendation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /serie-recomendations : Updates an existing serieRecomendation.
     *
     * @param serieRecomendation the serieRecomendation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serieRecomendation,
     * or with status 400 (Bad Request) if the serieRecomendation is not valid,
     * or with status 500 (Internal Server Error) if the serieRecomendation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/serie-recomendations")
    @Timed
    public ResponseEntity<SerieRecomendation> updateSerieRecomendation(@RequestBody SerieRecomendation serieRecomendation) throws URISyntaxException {
        log.debug("REST request to update SerieRecomendation : {}", serieRecomendation);
        if (serieRecomendation.getId() == null) {
            return createSerieRecomendation(serieRecomendation);
        }
        SerieRecomendation result = serieRecomendationRepository.save(serieRecomendation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serieRecomendation", serieRecomendation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /serie-recomendations : get all the serieRecomendations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serieRecomendations in body
     */
    @GetMapping("/serie-recomendations")
    @Timed
    public List<SerieRecomendation> getAllSerieRecomendations() {
        log.debug("REST request to get all SerieRecomendations");
        List<SerieRecomendation> serieRecomendations = serieRecomendationRepository.findAll();
        return serieRecomendations;
    }

    /**
     * GET  /serie-recomendations/:id : get the "id" serieRecomendation.
     *
     * @param id the id of the serieRecomendation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serieRecomendation, or with status 404 (Not Found)
     */
    @GetMapping("/serie-recomendations/{id}")
    @Timed
    public ResponseEntity<SerieRecomendation> getSerieRecomendation(@PathVariable Long id) {
        log.debug("REST request to get SerieRecomendation : {}", id);
        SerieRecomendation serieRecomendation = serieRecomendationRepository.findOne(id);
        return Optional.ofNullable(serieRecomendation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /serie-recomendations/:id : delete the "id" serieRecomendation.
     *
     * @param id the id of the serieRecomendation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/serie-recomendations/{id}")
    @Timed
    public ResponseEntity<Void> deleteSerieRecomendation(@PathVariable Long id) {
        log.debug("REST request to delete SerieRecomendation : {}", id);
        serieRecomendationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serieRecomendation", id.toString())).build();
    }

}
