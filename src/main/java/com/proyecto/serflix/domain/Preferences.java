package com.proyecto.serflix.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Integer value;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name = "preferences_movie_recomendation",
               joinColumns = @JoinColumn(name="preferences_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="movie_recomendations_id", referencedColumnName="ID"))
    private Set<MovieRecomendation> movieRecomendations = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "preferences_serie_recomendation",
               joinColumns = @JoinColumn(name="preferences_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="serie_recomendations_id", referencedColumnName="ID"))
    private Set<SerieRecomendation> serieRecomendations = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "preferences_request",
               joinColumns = @JoinColumn(name="preferences_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="requests_id", referencedColumnName="ID"))
    private Set<Request> requests = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Preferences type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Preferences name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public Preferences value(Integer value) {
        this.value = value;
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public Preferences user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<MovieRecomendation> getMovieRecomendations() {
        return movieRecomendations;
    }

    public Preferences movieRecomendations(Set<MovieRecomendation> movieRecomendations) {
        this.movieRecomendations = movieRecomendations;
        return this;
    }

    public Preferences addMovieRecomendation(MovieRecomendation movieRecomendation) {
        movieRecomendations.add(movieRecomendation);
        movieRecomendation.getPreferences().add(this);
        return this;
    }

    public Preferences removeMovieRecomendation(MovieRecomendation movieRecomendation) {
        movieRecomendations.remove(movieRecomendation);
        movieRecomendation.getPreferences().remove(this);
        return this;
    }

    public void setMovieRecomendations(Set<MovieRecomendation> movieRecomendations) {
        this.movieRecomendations = movieRecomendations;
    }

    public Set<SerieRecomendation> getSerieRecomendations() {
        return serieRecomendations;
    }

    public Preferences serieRecomendations(Set<SerieRecomendation> serieRecomendations) {
        this.serieRecomendations = serieRecomendations;
        return this;
    }

    public Preferences addSerieRecomendation(SerieRecomendation serieRecomendation) {
        serieRecomendations.add(serieRecomendation);
        serieRecomendation.getPreferences().add(this);
        return this;
    }

    public Preferences removeSerieRecomendation(SerieRecomendation serieRecomendation) {
        serieRecomendations.remove(serieRecomendation);
        serieRecomendation.getPreferences().remove(this);
        return this;
    }

    public void setSerieRecomendations(Set<SerieRecomendation> serieRecomendations) {
        this.serieRecomendations = serieRecomendations;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public Preferences requests(Set<Request> requests) {
        this.requests = requests;
        return this;
    }

    public Preferences addRequest(Request request) {
        requests.add(request);
        request.getPreferences().add(this);
        return this;
    }

    public Preferences removeRequest(Request request) {
        requests.remove(request);
        request.getPreferences().remove(this);
        return this;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Preferences preferences = (Preferences) o;
        if (preferences.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, preferences.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", name='" + name + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
