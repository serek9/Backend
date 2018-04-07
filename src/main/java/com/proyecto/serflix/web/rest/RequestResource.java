package com.proyecto.serflix.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proyecto.serflix.domain.Location;
import com.proyecto.serflix.domain.MovieRecomendation;
import com.proyecto.serflix.domain.Request;
import com.proyecto.serflix.repository.MovieRecomendationRepository;
import com.proyecto.serflix.repository.MovieRepository;
import com.proyecto.serflix.repository.RequestRepository;
import com.proyecto.serflix.service.MapsAPI.MapsDTOService;
import com.proyecto.serflix.service.RecommendationEngine;
import com.proyecto.serflix.service.RequestService;
import com.proyecto.serflix.service.WeatherDatabase.WeatherDTOService;
import com.proyecto.serflix.service.dto.MapsAPI.AddressDTO;
import com.proyecto.serflix.service.dto.WeatherDatabase.WeatherData;
import com.proyecto.serflix.web.rest.dto.RequestDTO;
import com.proyecto.serflix.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Request.
 */
@RestController
@RequestMapping("/api")
public class RequestResource {

    private final Logger log = LoggerFactory.getLogger(RequestResource.class);

    @Inject
    private MapsDTOService mapsDTOService;

    @Inject
    private RecommendationEngine recommendationEngine;

    @Inject
    private WeatherDTOService weatherDTOService;

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private RequestService requestService;

    @Inject
    private MovieRecomendationRepository movieRecomendationRepository;

    /**
     * POST  /requests : Create a new request.
     *
     * @param request the request to create
     * @return the ResponseEntity with status 201 (Created) and with body the new request, or with status 400 (Bad Request) if the request has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/requests")
    @Timed
    public ResponseEntity<Request> createRequest(@RequestBody Request request) throws URISyntaxException {
        log.debug("REST request to save Request : {}", request);
        if (request.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("request", "idexists", "A new request cannot already have an ID")).body(null);
        }
        Request result = requestRepository.save(request);
        return ResponseEntity.created(new URI("/api/requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("request", result.getId().toString()))
            .body(result);
    }

    @PostMapping("/newrequest")
    @Timed
    @Transactional
    public ResponseEntity<Request> createNewRequest(@RequestBody RequestDTO rdto) throws URISyntaxException {
        log.debug("REST request to save Request : {}", rdto);
        Request result = requestRepository.save(requestService.buildRequest(rdto));
        recommendationEngine.generateMovieRecommendations(result);
        return ResponseEntity.created(new URI("/api/requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("request", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requests : Updates an existing request.
     *
     * @param request the request to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated request,
     * or with status 400 (Bad Request) if the request is not valid,
     * or with status 500 (Internal Server Error) if the request couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/requests")
    @Timed
    public ResponseEntity<Request> updateRequest(@RequestBody Request request) throws URISyntaxException {
        log.debug("REST request to update Request : {}", request);
        if (request.getId() == null) {
            return createRequest(request);
        }
        Request result = requestRepository.save(request);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("request", request.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requests : get all the requests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of requests in body
     */
    @GetMapping("/requests")
    @Timed
    public List<Request> getAllRequests() {
        log.debug("REST request to get all Requests");
        List<Request> requests = requestRepository.findAllWithEagerRelationships();
        return requests;
    }

    @GetMapping("/requests/{id}/recommendations")
    @Timed
    public List<MovieRecomendation> getRecommendationsForRequest(@PathVariable Long id) {
        log.debug("REST request to get movie recommendation");
        //Request request = requestRepository.findOne(requestId);
        //List<MovieRecomendation> recommendations = movieRecomendationRepository.findByRequestIs(request);
        Request request = requestRepository.findOne(id);
        List<MovieRecomendation> recommendations = movieRecomendationRepository.findByRequestIs(request);
        return recommendations;
    }

    /**
     * GET  /requests/:id : get the "id" request.
     *
     * @param id the id of the request to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the request, or with status 404 (Not Found)
     */
    @GetMapping("/requests/{id}")
    @Timed
    public ResponseEntity<Request> getRequest(@PathVariable Long id) {
        log.debug("REST request to get Request : {}", id);
        Request request = requestRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(request)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/requests/geocode/{latlng:.*}")
    @Timed
    public ResponseEntity<AddressDTO> getGeocodeRequest(@PathVariable String latlng) {
        log.debug("REST request to get Request : {}", latlng);
        AddressDTO addressDTO = mapsDTOService.getGeocode(latlng);
        Location location = mapsDTOService.getLocation(latlng);


        /*String country = addressDTO.
            getResults().
            stream().
            filter(result -> result.getTypes().
                contains("country")).collect(Collectors.toList())
            .get(0).getFormattedAddress();
        System.out.println(country);

        String state = addressDTO.
            getResults().
            stream().
            filter(result -> result.getTypes().
                contains("administrative_area_level_1")).collect(Collectors.toList())
            .get(0).getFormattedAddress();
        System.out.println(state);

        String city = addressDTO.
            getResults().
            stream().
            filter(result -> result.getTypes().
                contains("administrative_area_level_2")).collect(Collectors.toList())
            .get(0).getFormattedAddress();
        System.out.println(city);*/


        return Optional.ofNullable(addressDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/requests/weather/{latlng}")
    @Timed
    public ResponseEntity<WeatherData> getWeatherRequest(@PathVariable String latlng) {
        //String latlng only for test, must change to a LocationDTO variable(Change in service too)
        log.debug("REST request to get Request : {}", latlng);
        WeatherData weatherData = weatherDTOService.getWeatherData(latlng);

        String currently = weatherData.getCurrently().getIcon().toString();
        //Tiempo actual
        System.out.println(currently);
        //Tiempo dentro de 48h
        String hourly = weatherData.getHourly().getData().get(0).getIcon().toString();
        System.out.println(hourly);
        //Tiempo lunes de esta semana
        String daily = weatherData.getDaily().getData().get(0).getIcon().toString();
        System.out.println(daily);
        //Tiempo lunes de la semana siguiente
        String daily2 = weatherData.getDaily().getData().get(7).getIcon().toString();
        System.out.println(daily2);

        return Optional.ofNullable(weatherData)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /requests/:id : delete the "id" request.
     *
     * @param id the id of the request to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        log.debug("REST request to delete Request : {}", id);
        requestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("request", id.toString())).build();
    }

}
