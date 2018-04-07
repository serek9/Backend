package com.proyecto.serflix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Serie.
 */
@Entity
@Table(name = "serie")
public class Serie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_external_api")
    private Long idExternalApi;

    @OneToMany(mappedBy = "serie")
    @JsonIgnore
    private Set<SerieRecomendation> serieRecomendations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Serie name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdExternalApi() {
        return idExternalApi;
    }

    public Serie idExternalApi(Long idExternalApi) {
        this.idExternalApi = idExternalApi;
        return this;
    }

    public void setIdExternalApi(Long idExternalApi) {
        this.idExternalApi = idExternalApi;
    }

    public Set<SerieRecomendation> getSerieRecomendations() {
        return serieRecomendations;
    }

    public Serie serieRecomendations(Set<SerieRecomendation> serieRecomendations) {
        this.serieRecomendations = serieRecomendations;
        return this;
    }

    public Serie addSerieRecomendation(SerieRecomendation serieRecomendation) {
        serieRecomendations.add(serieRecomendation);
        serieRecomendation.setSerie(this);
        return this;
    }

    public Serie removeSerieRecomendation(SerieRecomendation serieRecomendation) {
        serieRecomendations.remove(serieRecomendation);
        serieRecomendation.setSerie(null);
        return this;
    }

    public void setSerieRecomendations(Set<SerieRecomendation> serieRecomendations) {
        this.serieRecomendations = serieRecomendations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Serie serie = (Serie) o;
        if (serie.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Serie{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", idExternalApi='" + idExternalApi + "'" +
            '}';
    }
}
