package com.proyecto.serflix.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proyecto.serflix.domain.Forecast;

import com.proyecto.serflix.repository.ForecastRepository;
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
 * REST controller for managing Forecast.
 */
@RestController
@RequestMapping("/api")
public class ForecastResource {

    private final Logger log = LoggerFactory.getLogger(ForecastResource.class);
        
    @Inject
    private ForecastRepository forecastRepository;

    /**
     * POST  /forecasts : Create a new forecast.
     *
     * @param forecast the forecast to create
     * @return the ResponseEntity with status 201 (Created) and with body the new forecast, or with status 400 (Bad Request) if the forecast has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/forecasts")
    @Timed
    public ResponseEntity<Forecast> createForecast(@RequestBody Forecast forecast) throws URISyntaxException {
        log.debug("REST request to save Forecast : {}", forecast);
        if (forecast.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("forecast", "idexists", "A new forecast cannot already have an ID")).body(null);
        }
        Forecast result = forecastRepository.save(forecast);
        return ResponseEntity.created(new URI("/api/forecasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("forecast", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /forecasts : Updates an existing forecast.
     *
     * @param forecast the forecast to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated forecast,
     * or with status 400 (Bad Request) if the forecast is not valid,
     * or with status 500 (Internal Server Error) if the forecast couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/forecasts")
    @Timed
    public ResponseEntity<Forecast> updateForecast(@RequestBody Forecast forecast) throws URISyntaxException {
        log.debug("REST request to update Forecast : {}", forecast);
        if (forecast.getId() == null) {
            return createForecast(forecast);
        }
        Forecast result = forecastRepository.save(forecast);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("forecast", forecast.getId().toString()))
            .body(result);
    }

    /**
     * GET  /forecasts : get all the forecasts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of forecasts in body
     */
    @GetMapping("/forecasts")
    @Timed
    public List<Forecast> getAllForecasts() {
        log.debug("REST request to get all Forecasts");
        List<Forecast> forecasts = forecastRepository.findAll();
        return forecasts;
    }

    /**
     * GET  /forecasts/:id : get the "id" forecast.
     *
     * @param id the id of the forecast to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the forecast, or with status 404 (Not Found)
     */
    @GetMapping("/forecasts/{id}")
    @Timed
    public ResponseEntity<Forecast> getForecast(@PathVariable Long id) {
        log.debug("REST request to get Forecast : {}", id);
        Forecast forecast = forecastRepository.findOne(id);
        return Optional.ofNullable(forecast)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /forecasts/:id : delete the "id" forecast.
     *
     * @param id the id of the forecast to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/forecasts/{id}")
    @Timed
    public ResponseEntity<Void> deleteForecast(@PathVariable Long id) {
        log.debug("REST request to delete Forecast : {}", id);
        forecastRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("forecast", id.toString())).build();
    }

}
