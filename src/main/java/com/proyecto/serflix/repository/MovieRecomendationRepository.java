package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.MovieRecomendation;
import com.proyecto.serflix.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the MovieRecomendation entity.
 */
@SuppressWarnings("unused")
public interface MovieRecomendationRepository extends JpaRepository<MovieRecomendation,Long> {
    List<MovieRecomendation> findByRequestIs(Request request);
}

