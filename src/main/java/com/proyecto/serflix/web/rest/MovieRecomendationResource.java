package com.proyecto.serflix.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.proyecto.serflix.domain.*;
import com.proyecto.serflix.domain.enumeration.Company;
import com.proyecto.serflix.repository.*;
import com.proyecto.serflix.service.LearningEngine;
import com.proyecto.serflix.service.MovieDatabase.MovieDTOService;
import com.proyecto.serflix.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing MovieRecomendation.
 */
@RestController
@RequestMapping("/api")
public class MovieRecomendationResource {

    private final Logger log = LoggerFactory.getLogger(MovieRecomendationResource.class);

    @Inject
    private MovieRecomendationRepository movieRecomendationRepository;

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private PreferencesRepository preferencesRepository;

    @Inject
    private LearningEngine learningEngine;

    @Inject
    private MovieDTOService movieDTOService;

    /**
     * POST  /movie-recomendations : Create a new movieRecomendation.
     *
     * @param movieRecomendation the movieRecomendation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movieRecomendation, or with status 400 (Bad Request) if the movieRecomendation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/movie-recomendations")
    @Timed
    public ResponseEntity<MovieRecomendation> createMovieRecomendation(@RequestBody MovieRecomendation movieRecomendation) throws URISyntaxException {
        log.debug("REST request to save MovieRecomendation : {}", movieRecomendation);
        if (movieRecomendation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movieRecomendation", "idexists", "A new movieRecomendation cannot already have an ID")).body(null);
        }
        MovieRecomendation result = movieRecomendationRepository.save(movieRecomendation);
        return ResponseEntity.created(new URI("/api/movie-recomendations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("movieRecomendation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movie-recomendations : Updates an existing movieRecomendation.
     *
     * @param movieRecomendation the movieRecomendation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movieRecomendation,
     * or with status 400 (Bad Request) if the movieRecomendation is not valid,
     * or with status 500 (Internal Server Error) if the movieRecomendation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/movie-recomendations")
    @Timed
    public ResponseEntity<MovieRecomendation> updateMovieRecomendation(@RequestBody MovieRecomendation movieRecomendation) throws URISyntaxException {
        log.debug("REST request to update MovieRecomendation : {}", movieRecomendation);
        if (movieRecomendation.getId() == null) {
            return createMovieRecomendation(movieRecomendation);
        }
        Request request = movieRecomendation.getRequest();
        Set<Preferences> preferencesSet = movieRecomendation.getPreferences();

        movieRecomendation.setMovieDTO(movieRepository.findByName(movieRecomendation.getMovieDTO().getName()).get(0));
        requestRepository.save(request);
        preferencesRepository.save(preferencesSet);

        MovieRecomendation result = movieRecomendationRepository.save(movieRecomendation);

        //Aprender
        learningEngine.learnFromRecommendation(movieRecomendation);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("movieRecomendation", movieRecomendation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movie-recomendations : get all the movieRecomendations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of movieRecomendations in body
     */
    @GetMapping("/movie-recomendations")
    @Timed
    public List<MovieRecomendation> getAllMovieRecomendations() {
        log.debug("REST request to get all MovieRecomendations");
        List<MovieRecomendation> movieRecomendations = movieRecomendationRepository.findAll();
        return movieRecomendations;
    }

    /**
     * GET  /movie-recomendations/:id : get the "id" movieRecomendation.
     *
     * @param id the id of the movieRecomendation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movieRecomendation, or with status 404 (Not Found)
     */
    @GetMapping("/movie-recomendations/{id}")
    @Timed
    public ResponseEntity<MovieRecomendation> getMovieRecomendation(@PathVariable Long id) {
        log.debug("REST request to get MovieRecomendation : {}", id);
        MovieRecomendation movieRecomendation = movieRecomendationRepository.findOne(id);
        return Optional.ofNullable(movieRecomendation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/movie-recomendations/test-inicial")
    @Timed
    public List<MovieRecomendation> getTestInicial() {
        /*
            El resplandor
            Titanic
            Como Dios
            La Jungla de cristal
            Matchpoints
            Ali
            Club de la lucha
            avatar
            salvar soldado ryan
            grease
         */
        Request request = new Request();
        request.setCompany(Company.ALONE);
        request.setCreationDate(ZonedDateTime.now());
        request.setName("Test inicial");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userRepository.findByLogin(auth.getName());
        request.setUserRequester(u);
        request.setViewDate(ZonedDateTime.now());
        requestRepository.save(request);

        List<MovieRecomendation> movieRecomendations = new ArrayList<>();
        Movie movie = MovieDTOService.getMovie(694).toMovie();
        movieRepository.save(movie);
        MovieRecomendation movieRecomendation = new MovieRecomendation(movie, request);
        movieRecomendationRepository.save(movieRecomendation);
        movieRecomendations.add(movieRecomendation);

        Movie movie2 = MovieDTOService.getMovie(597).toMovie();
        movieRepository.save(movie2);
        MovieRecomendation movieRecomendation2 = new MovieRecomendation(movie2, request);
        movieRecomendationRepository.save(movieRecomendation2);
        movieRecomendations.add(movieRecomendation2);


        Movie movie3 = MovieDTOService.getMovie(310).toMovie();
        movieRepository.save(movie3);
        MovieRecomendation movieRecomendation3 = new MovieRecomendation(movie3, request);
        movieRecomendationRepository.save(movieRecomendation3);
        movieRecomendations.add(movieRecomendation3);


        Movie movie4 = MovieDTOService.getMovie(562).toMovie();
        movieRepository.save(movie4);
        MovieRecomendation movieRecomendation4 = new MovieRecomendation(movie4, request);
        movieRecomendationRepository.save(movieRecomendation4);
        movieRecomendations.add(movieRecomendation4);


        Movie movie5 = MovieDTOService.getMovie(116).toMovie();
        movieRepository.save(movie5);
        MovieRecomendation movieRecomendation5 = new MovieRecomendation(movie5, request);
        movieRecomendationRepository.save(movieRecomendation5);
        movieRecomendations.add(movieRecomendation5);


        Movie movie6 = MovieDTOService.getMovie(8489).toMovie();
        movieRepository.save(movie6);
        MovieRecomendation movieRecomendation6 = new MovieRecomendation(movie6, request);
        movieRecomendationRepository.save(movieRecomendation6);
        movieRecomendations.add(movieRecomendation6);


        Movie movie7 = MovieDTOService.getMovie(550).toMovie();
        movieRepository.save(movie7);
        MovieRecomendation movieRecomendation7 = new MovieRecomendation(movie7, request);
        movieRecomendationRepository.save(movieRecomendation7);
        movieRecomendations.add(movieRecomendation7);


        Movie movie8 = MovieDTOService.getMovie(19995).toMovie();
        movieRepository.save(movie8);
        MovieRecomendation movieRecomendation8 = new MovieRecomendation(movie8, request);
        movieRecomendationRepository.save(movieRecomendation8);
        movieRecomendations.add(movieRecomendation8);


        Movie movie9 = MovieDTOService.getMovie(857).toMovie();
        movieRepository.save(movie9);
        MovieRecomendation movieRecomendation9 = new MovieRecomendation(movie9, request);
        movieRecomendationRepository.save(movieRecomendation9);
        movieRecomendations.add(movieRecomendation9);


        Movie movie10 = MovieDTOService.getMovie(621).toMovie();
        movieRepository.save(movie10);
        MovieRecomendation movieRecomendation10 = new MovieRecomendation(movie10, request);
        movieRecomendationRepository.save(movieRecomendation10);
        movieRecomendations.add(movieRecomendation10);



        return movieRecomendations;
    }

    /**
     * DELETE  /movie-recomendations/:id : delete the "id" movieRecomendation.
     *
     * @param id the id of the movieRecomendation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/movie-recomendations/{id}")
    @Timed
    public ResponseEntity<Void> deleteMovieRecomendation(@PathVariable Long id) {

        log.debug("REST request to delete MovieRecomendation : {}", id);
        movieRecomendationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(
            "movieRecomendation", id.toString())).build();
    }

}
