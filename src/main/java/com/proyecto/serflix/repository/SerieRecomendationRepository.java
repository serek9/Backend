package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.SerieRecomendation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SerieRecomendation entity.
 */
@SuppressWarnings("unused")
public interface SerieRecomendationRepository extends JpaRepository<SerieRecomendation,Long> {

}
