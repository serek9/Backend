package com.proyecto.serflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.serflix.domain.enumeration.RecomendationResult;
import com.proyecto.serflix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A MovieRecomendation.
 */
@Entity
@Table(name = "movie_recomendation")
public class MovieRecomendation implements Serializable {
    @Transient
    @Autowired
    private UserRepository userRepository;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "recomendation_result")
    private RecomendationResult recomendationResult;

    @ManyToOne
    private Movie movieDTO;

    @ManyToOne
    private Request request;

    @ManyToMany(mappedBy = "movieRecomendations")
    @JsonIgnore
    private Set<Preferences> preferences = new HashSet<>();

    public MovieRecomendation() {
    }

    public MovieRecomendation(RecomendationResult recomendationResult, Movie movieDTO, Request request, Set<Preferences> preferences) {
        this.recomendationResult = recomendationResult;
        this.movieDTO = movieDTO;
        this.request = request;
        this.preferences = preferences;
    }

    public MovieRecomendation(Movie movieDTO, Request request) {
        this.movieDTO = movieDTO;
        this.request = request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecomendationResult getRecomendationResult() {
        return recomendationResult;
    }

    public MovieRecomendation recomendationResult(RecomendationResult recomendationResult) {
        this.recomendationResult = recomendationResult;
        return this;
    }

    public void setRecomendationResult(RecomendationResult recomendationResult) {
        this.recomendationResult = recomendationResult;
    }

    public Movie getMovieDTO() {
        return movieDTO;
    }

    public MovieRecomendation movieDTO(Movie movie) {
        this.movieDTO = movie;
        return this;
    }

    public void setMovieDTO(Movie movie) {
        this.movieDTO = movie;
    }

    public Request getRequest() {
        return request;
    }

    public MovieRecomendation request(Request request) {
        this.request = request;
        return this;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Set<Preferences> getPreferences() {
        return preferences;
    }

    public MovieRecomendation preferences(Set<Preferences> preferences) {
        this.preferences = preferences;
        return this;
    }

    public MovieRecomendation addPreferences(Preferences preferences) {
        this.preferences.add(preferences);
        preferences.getMovieRecomendations().add(this);
        return this;
    }

    public MovieRecomendation removePreferences(Preferences preferences) {
        this.preferences.remove(preferences);
        preferences.getMovieRecomendations().remove(this);
        return this;
    }

    public void setPreferences(Set<Preferences> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieRecomendation movieRecomendation = (MovieRecomendation) o;
        if (movieRecomendation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, movieRecomendation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MovieRecomendation{" +
            "id=" + id +
            ", recomendationResult='" + recomendationResult + "'" +
            '}';
    }
}
