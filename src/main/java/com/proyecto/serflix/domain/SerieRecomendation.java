package com.proyecto.serflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.serflix.domain.enumeration.RecomendationResult;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A SerieRecomendation.
 */
@Entity
@Table(name = "serie_recomendation")
public class SerieRecomendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "recomendation_result")
    private RecomendationResult recomendationResult;

    @ManyToOne
    private Serie serie;

    @ManyToOne
    private Request request;

    @ManyToMany(mappedBy = "serieRecomendations")
    @JsonIgnore
    private Set<Preferences> preferences = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecomendationResult getRecomendationResult() {
        return recomendationResult;
    }

    public SerieRecomendation recomendationResult(RecomendationResult recomendationResult) {
        this.recomendationResult = recomendationResult;
        return this;
    }

    public void setRecomendationResult(RecomendationResult recomendationResult) {
        this.recomendationResult = recomendationResult;
    }

    public Serie getSerie() {
        return serie;
    }

    public SerieRecomendation serie(Serie serie) {
        this.serie = serie;
        return this;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Request getRequest() {
        return request;
    }

    public SerieRecomendation request(Request request) {
        this.request = request;
        return this;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Set<Preferences> getPreferences() {
        return preferences;
    }

    public SerieRecomendation preferences(Set<Preferences> preferences) {
        this.preferences = preferences;
        return this;
    }

    public SerieRecomendation addPreferences(Preferences preferences) {
        this.preferences.add(preferences);
        preferences.getSerieRecomendations().add(this);
        return this;
    }

    public SerieRecomendation removePreferences(Preferences preferences) {
        this.preferences.remove(preferences);
        preferences.getSerieRecomendations().remove(this);
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
        SerieRecomendation serieRecomendation = (SerieRecomendation) o;
        if (serieRecomendation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serieRecomendation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SerieRecomendation{" +
            "id=" + id +
            ", recomendationResult='" + recomendationResult + "'" +
            '}';
    }
}
