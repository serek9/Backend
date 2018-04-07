package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.Forecast;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Forecast entity.
 */
@SuppressWarnings("unused")
public interface ForecastRepository extends JpaRepository<Forecast,Long> {

}
