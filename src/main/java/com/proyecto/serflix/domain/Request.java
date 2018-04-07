package com.proyecto.serflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.serflix.domain.enumeration.Company;
import com.proyecto.serflix.domain.enumeration.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Request.
 */
@Entity
@Table(name = "request")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "name")
    private String name;

    @Column(name = "view_date")
    private ZonedDateTime viewDate;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "company")
    private Company company;

    @ManyToOne
    private User userRequester;

    @ManyToOne
    private Location location;

    @ManyToMany
    @JoinTable(name = "request_user_guest",
               joinColumns = @JoinColumn(name="requests_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="user_guests_id", referencedColumnName="ID"))
    private Set<User> userGuests = new HashSet<>();

    @OneToMany(mappedBy = "request")
    @JsonIgnore
    private Set<Forecast> forecasts = new HashSet<>();

    @OneToMany(mappedBy = "request")
    @JsonIgnore
    private Set<MovieRecomendation> movieRecomendations = new HashSet<>();

    @OneToMany(mappedBy = "request")
    @JsonIgnore
    private Set<SerieRecomendation> serieRecomendations = new HashSet<>();

    @ManyToMany(mappedBy = "requests")
    @JsonIgnore
    private Set<Preferences> preferences = new HashSet<>();

    public Request(Type type, String name, ZonedDateTime viewDate, ZonedDateTime creationDate, Company company, User userRequester, Location location, Set<User> userGuests, Set<Forecast> forecasts, Set<MovieRecomendation> movieRecomendations, Set<SerieRecomendation> serieRecomendations, Set<Preferences> preferences) {
        this.type = type;
        this.name = name;
        this.viewDate = viewDate;
        this.creationDate = creationDate;
        this.company = company;
        this.userRequester = userRequester;
        this.location = location;
        this.userGuests = userGuests;
        this.forecasts = forecasts;
        this.movieRecomendations = movieRecomendations;
        this.serieRecomendations = serieRecomendations;
        this.preferences = preferences;
    }

    public Request(Type type, String name, ZonedDateTime viewDate, ZonedDateTime creationDate, Company company, User userRequester, Location location, Set<Forecast> forecasts) {
        this.type = type;
        this.name = name;
        this.viewDate = viewDate;
        this.creationDate = creationDate;
        this.company = company;
        this.userRequester = userRequester;
        this.location = location;
        this.userGuests = userGuests;
        this.forecasts = forecasts;
    }

    public Request() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public Request type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Request name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getViewDate() {
        return viewDate;
    }

    public Request viewDate(ZonedDateTime viewDate) {
        this.viewDate = viewDate;
        return this;
    }

    public void setViewDate(ZonedDateTime viewDate) {
        this.viewDate = viewDate;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Request creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Company getCompany() {
        return company;
    }

    public Request company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUserRequester() {
        return userRequester;
    }

    public Request userRequester(User user) {
        this.userRequester = user;
        return this;
    }

    public void setUserRequester(User user) {
        this.userRequester = user;
    }

    public Location getLocation() {
        return location;
    }

    public Request location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<User> getUserGuests() {
        return userGuests;
    }

    public Request userGuests(Set<User> users) {
        this.userGuests = users;
        return this;
    }

    public Request addUserGuest(User user) {
        userGuests.add(user);
        return this;
    }

    public Request removeUserGuest(User user) {
        userGuests.remove(user);
        return this;
    }

    public void setUserGuests(Set<User> users) {
        this.userGuests = users;
    }

    public Set<Forecast> getForecasts() {
        return forecasts;
    }

    public Request forecasts(Set<Forecast> forecasts) {
        this.forecasts = forecasts;
        return this;
    }

    public Request addForecast(Forecast forecast) {
        forecasts.add(forecast);
        forecast.setRequest(this);
        return this;
    }

    public Request removeForecast(Forecast forecast) {
        forecasts.remove(forecast);
        forecast.setRequest(null);
        return this;
    }

    public void setForecasts(Set<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public Set<MovieRecomendation> getMovieRecomendations() {
        return movieRecomendations;
    }

    public Request movieRecomendations(Set<MovieRecomendation> movieRecomendations) {
        this.movieRecomendations = movieRecomendations;
        return this;
    }

    public Request addMovieRecomendation(MovieRecomendation movieRecomendation) {
        movieRecomendations.add(movieRecomendation);
        movieRecomendation.setRequest(this);
        return this;
    }

    public Request removeMovieRecomendation(MovieRecomendation movieRecomendation) {
        movieRecomendations.remove(movieRecomendation);
        movieRecomendation.setRequest(null);
        return this;
    }

    public void setMovieRecomendations(Set<MovieRecomendation> movieRecomendations) {
        this.movieRecomendations = movieRecomendations;
    }

    public Set<SerieRecomendation> getSerieRecomendations() {
        return serieRecomendations;
    }

    public Request serieRecomendations(Set<SerieRecomendation> serieRecomendations) {
        this.serieRecomendations = serieRecomendations;
        return this;
    }

    public Request addSerieRecomendation(SerieRecomendation serieRecomendation) {
        serieRecomendations.add(serieRecomendation);
        serieRecomendation.setRequest(this);
        return this;
    }

    public Request removeSerieRecomendation(SerieRecomendation serieRecomendation) {
        serieRecomendations.remove(serieRecomendation);
        serieRecomendation.setRequest(null);
        return this;
    }

    public void setSerieRecomendations(Set<SerieRecomendation> serieRecomendations) {
        this.serieRecomendations = serieRecomendations;
    }

    public Set<Preferences> getPreferences() {
        return preferences;
    }

    public Request preferences(Set<Preferences> preferences) {
        this.preferences = preferences;
        return this;
    }

    public Request addPreferences(Preferences preferences) {
        this.preferences.add(preferences);
        preferences.getRequests().add(this);
        return this;
    }

    public Request removePreferences(Preferences preferences) {
        this.preferences.remove(preferences);
        preferences.getRequests().remove(this);
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
        Request request = (Request) o;
        if (request.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, request.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Request{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", name='" + name + "'" +
            ", viewDate='" + viewDate + "'" +
            ", creationDate='" + creationDate + "'" +
            ", company='" + company + "'" +
            '}';
    }
}
