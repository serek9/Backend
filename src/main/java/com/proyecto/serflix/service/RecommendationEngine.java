package com.proyecto.serflix.service;

import com.proyecto.serflix.domain.*;
import com.proyecto.serflix.domain.enumeration.Weather;
import com.proyecto.serflix.repository.MovieRecomendationRepository;
import com.proyecto.serflix.repository.MovieRepository;
import com.proyecto.serflix.repository.PreferencesRepository;
import com.proyecto.serflix.service.MovieDatabase.DiscoverService;
import com.proyecto.serflix.service.MovieDatabase.MovieDTOService;
import com.proyecto.serflix.service.WeatherDatabase.WeatherDTOService;
import com.proyecto.serflix.service.dto.MovieDatabase.Keyword;
import com.proyecto.serflix.service.dto.MovieDatabase.MovieDTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class RecommendationEngine {
    @Inject
    private MovieRecomendationRepository movieRecomendationRepository;

    @Inject
    private MovieRepository movieRepository;

    @Inject
    private MovieDTOService movieDTOService;

    @Inject
    private WeatherDTOService weatherDTOService;

    @Inject
    private DiscoverService discoverService;

    @Inject
    private PreferencesRepository preferencesRepository;

    private List<MovieDTO> movieList;

    public boolean generateMovieRecommendations(Request request){

        MovieDTOService movieDTOService = new MovieDTOService();
        Forecast forecast = weatherDTOService.getCurrentForecast(request.getLocation().getLatLon());

        List<Preferences> userPreferences = preferencesRepository.findByUserIsCurrentUser();

        //    CLEAR,RAIN,SNOW,CLOUDY,PARTLY_CLOUDY
        switch (request.getCompany()){

            case ALONE:
                //movieList = movieDTOService.getMostPopular();
                if (!forecast.getWeather().equals(Weather.CLEAR)){
                    movieList = movieDTOService.getComedyFilms();
                }
                if(!forecast.getWeather().equals(Weather.RAIN)){
                    movieList = movieDTOService.getRainyFilms();
                }
                if(!forecast.getWeather().equals(Weather.SNOW)){
                    movieList = discoverService.getFamilyMovies();
                }
                if(!forecast.getWeather().equals(Weather.CLOUDY)) {
                    movieList = movieDTOService.getMisteryFilms();
                }
                break;

            case PARTNER:
                if (!forecast.getWeather().equals(Weather.CLEAR)){
                    movieList = movieDTOService.getFictionFilms();
                }
                if(!forecast.getWeather().equals(Weather.RAIN)){
                    movieList = movieDTOService.getRainyFilms();
                }
                if(!forecast.getWeather().equals(Weather.SNOW)){
                    movieList = movieDTOService.getRomanticFilms();
                }
                if(!forecast.getWeather().equals(Weather.CLOUDY)) {
                    movieList = movieDTOService.getMisteryFilms();
                }
                break;

            case FAMILY:
                if (!forecast.getWeather().equals(Weather.CLEAR)){
                    movieList = discoverService.getFamilyMovies();
                }
                if(!forecast.getWeather().equals(Weather.RAIN)){
                    movieList = movieDTOService.getFictionFilms();
                }
                if(!forecast.getWeather().equals(Weather.SNOW)){
                    movieList = discoverService.getFamilyMovies();
                }
                if(!forecast.getWeather().equals(Weather.CLOUDY)) {
                    movieList = movieDTOService.getMisteryFilms();
                }
                break;

            case FRIENDS:
                if (!forecast.getWeather().equals(Weather.CLEAR)){
                    movieList = movieDTOService.getComedyFilms();
                }
                if(!forecast.getWeather().equals(Weather.RAIN)){
                    movieList = movieDTOService.getRainyFilms();
                }
                if(!forecast.getWeather().equals(Weather.SNOW)){
                    movieList = movieDTOService.getFictionFilms();
                }
                if(!forecast.getWeather().equals(Weather.CLOUDY)) {
                    movieList = movieDTOService.getMisteryFilms();
                }
                break;

            case ANOTHER_USER:
                movieList = discoverService.getKidsMovies();
                break;

            default:
                movieList = movieDTOService.getMostPopular();
                break;
        }


        for(MovieDTO movieDTO : movieList){
            String description = movieDTO.getOverview();
            //CUTRE!!!! VER COMO ARREGLAR PARA QUE PERMITA SINPOSIS DE MAS DE 255 CARCTERES!!!!!
            if (description.length() > 244){
                description = description.substring(0,244);
            }
            String tags = "";
            if (movieDTO.getId() != null){
                List<Keyword> keyWordsList = movieDTOService.getMovieKeywords(movieDTO.getId());

            if (keyWordsList != null && keyWordsList.size() > 0){
                for (int i = 0; i < 5; i++) {
                    if (keyWordsList.size() > i){
                        if (i != 0){
                            tags += ", "+keyWordsList.get(i).getName();
                        }else{
                            tags += keyWordsList.get(i).getName();
                        }
                    }
                }
            }
            Movie movie;
            if (movieRepository.findByName(movieDTO.getTitle()).size() > 0){
                movie = movieRepository.findByName(movieDTO.getTitle()).get(0);

            }else{
                movie = new Movie(movieDTO.getTitle(), Long.valueOf(movieDTO.getId()), movieDTO.getPosterPath(), "Cast", tags, description, movieDTO.getReleaseDate());
                movie.setIdExternalApi(Long.valueOf(movieDTO.getId()));
                movieRepository.save(movie);
            }
            MovieRecomendation recomendation = new MovieRecomendation(null, movie, request, null);
            movieRecomendationRepository.save(recomendation);
            }
        }
        return true;
    }
}
