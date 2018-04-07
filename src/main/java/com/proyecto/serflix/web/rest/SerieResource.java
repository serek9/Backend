package com.proyecto.serflix.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proyecto.serflix.domain.Serie;

import com.proyecto.serflix.repository.SerieRepository;
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
 * REST controller for managing Serie.
 */
@RestController
@RequestMapping("/api")
public class SerieResource {

    private final Logger log = LoggerFactory.getLogger(SerieResource.class);
        
    @Inject
    private SerieRepository serieRepository;

    /**
     * POST  /series : Create a new serie.
     *
     * @param serie the serie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serie, or with status 400 (Bad Request) if the serie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/series")
    @Timed
    public ResponseEntity<Serie> createSerie(@RequestBody Serie serie) throws URISyntaxException {
        log.debug("REST request to save Serie : {}", serie);
        if (serie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serie", "idexists", "A new serie cannot already have an ID")).body(null);
        }
        Serie result = serieRepository.save(serie);
        return ResponseEntity.created(new URI("/api/series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serie", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /series : Updates an existing serie.
     *
     * @param serie the serie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serie,
     * or with status 400 (Bad Request) if the serie is not valid,
     * or with status 500 (Internal Server Error) if the serie couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/series")
    @Timed
    public ResponseEntity<Serie> updateSerie(@RequestBody Serie serie) throws URISyntaxException {
        log.debug("REST request to update Serie : {}", serie);
        if (serie.getId() == null) {
            return createSerie(serie);
        }
        Serie result = serieRepository.save(serie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serie", serie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /series : get all the series.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of series in body
     */
    @GetMapping("/series")
    @Timed
    public List<Serie> getAllSeries() {
        log.debug("REST request to get all Series");
        List<Serie> series = serieRepository.findAll();
        return series;
    }

    /**
     * GET  /series/:id : get the "id" serie.
     *
     * @param id the id of the serie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serie, or with status 404 (Not Found)
     */
    @GetMapping("/series/{id}")
    @Timed
    public ResponseEntity<Serie> getSerie(@PathVariable Long id) {
        log.debug("REST request to get Serie : {}", id);
        Serie serie = serieRepository.findOne(id);
        return Optional.ofNullable(serie)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /series/:id : delete the "id" serie.
     *
     * @param id the id of the serie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/series/{id}")
    @Timed
    public ResponseEntity<Void> deleteSerie(@PathVariable Long id) {
        log.debug("REST request to delete Serie : {}", id);
        serieRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serie", id.toString())).build();
    }

}
