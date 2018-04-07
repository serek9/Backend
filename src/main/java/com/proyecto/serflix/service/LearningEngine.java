package com.proyecto.serflix.service;

import com.proyecto.serflix.domain.MovieRecomendation;
import com.proyecto.serflix.domain.Preferences;
import com.proyecto.serflix.domain.User;
import com.proyecto.serflix.repository.PreferencesRepository;
import com.proyecto.serflix.repository.UserRepository;
import com.proyecto.serflix.service.MovieDatabase.MovieDTOService;
import com.proyecto.serflix.service.dto.MovieDatabase.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

@Service
public class LearningEngine {
    @Autowired
    private MovieDTOService movieDTOService;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private UserRepository userRepository;


    private int movieId;
    private MovieDTO movie;
    private String title;
    private List<ProductionCompany> productionCompanies;
    private List<ProductionCountry> productionCountries;
    private List<Genre> genres;
    private List<Keyword> keywords;
    private String releaseYear;
    private Credits credits;
    private List<Cast> cast;
    private List<Crew> crew;
    List<String> preferencesStr;

    public LearningEngine() {

    }

    public void learnFromRecommendation(MovieRecomendation recommendation){
        preferencesStr = new ArrayList<>();
        //Get all relevant data from movie that we'll need
        movieId = toIntExact(recommendation.getMovieDTO().getIdExternalApi());
        movie = movieDTOService.getMovie(movieId);

        title = movie.getTitle();
        preferencesStr.add(title);

        releaseYear = movie.getReleaseDate().substring(0,4);
        preferencesStr.add(releaseYear);

        productionCompanies = movie.getProductionCompanies();
        productionCompanies.forEach(productionCompany -> preferencesStr.add(productionCompany.getName()));

        productionCountries = movie.getProductionCountries();
        productionCountries.forEach(productionCountry -> preferencesStr.add(productionCountry.getName()));

        genres = movie.getGenres();
        genres.forEach(genre -> preferencesStr.add(genre.getName()));

        keywords = movieDTOService.getMovieKeywords(movieId);
        keywords.forEach(keyword -> preferencesStr.add(keyword.getName()));

        credits = movieDTOService.getMoviecredits(movieId);
        cast = credits.getCast();
        cast.forEach(c -> preferencesStr.add(c.getName()));
        crew = credits.getCrew();
        crew.forEach(c -> preferencesStr.add(c.getName()));

        //Update or insert each data gathered before and the movie as well into the user's preferences (add or take X points)
        int points = 0;
        switch (recommendation.getRecomendationResult()){
            case ACCEPTED:
                points = 5;
                break;
            case PRESELECTED:
                points = 3;
                break;
            case REJECTED:
                points = -2;
                break;
            case WATCHED_DISLIKED:
                points = -4;
                break;
            case WATCHED_LIKED:
                points = 7;
                break;
        }

        generatePreferences(points);
    }

    public void generatePreferences(int points){
        List<Preferences> preferences = new ArrayList<>();
        for (String preferenceStr : preferencesStr){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User u = userRepository.findByLogin(auth.getName());
            List<Preferences> preferencesList = preferencesRepository.findByNameAndUser(preferenceStr, u);
            Preferences p;
            if (preferencesList.size() > 0){
                p = preferencesList.get(0);
                p.setValue(p.getValue()+points);
            }else{
                p = new Preferences();
                p.setName(preferenceStr);
                p.setValue(points);
                p.setUser(u);
            }
            preferences.add(p);
            preferencesRepository.save(p);
        }
    }
}
