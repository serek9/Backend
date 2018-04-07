package com.proyecto.serflix.domain;


import com.proyecto.serflix.domain.enumeration.Weather;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Forecast.
 */
@Entity
@Table(name = "forecast")
public class Forecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "temperature")
    private Double temperature;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather")
    private Weather weather;

    @ManyToOne
    private Request request;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Forecast temperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public Forecast() {
    }

    public Forecast(Double temperature, Weather weather) {
        this.temperature = temperature;
        this.weather = weather;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Weather getWeather() {
        return weather;
    }

    public Forecast weather(Weather weather) {
        this.weather = weather;
        return this;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Request getRequest() {
        return request;
    }

    public Forecast request(Request request) {
        this.request = request;
        return this;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Forecast forecast = (Forecast) o;
        if (forecast.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, forecast.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Forecast{" +
            "id=" + id +
            ", temperature='" + temperature + "'" +
            ", weather='" + weather + "'" +
            '}';
    }
}
