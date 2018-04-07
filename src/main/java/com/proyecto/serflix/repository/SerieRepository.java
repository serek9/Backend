package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.Serie;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Serie entity.
 */
@SuppressWarnings("unused")
public interface SerieRepository extends JpaRepository<Serie,Long> {

}
