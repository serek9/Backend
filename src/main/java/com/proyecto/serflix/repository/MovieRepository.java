package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Movie entity.
 */
@SuppressWarnings("unused")
public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByName(String name);
}
