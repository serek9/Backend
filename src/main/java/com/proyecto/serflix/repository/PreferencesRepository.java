package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.Preferences;
import com.proyecto.serflix.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Preferences entity.
 */
@SuppressWarnings("unused")
public interface PreferencesRepository extends JpaRepository<Preferences,Long> {

    @Query("select preferences from Preferences preferences where preferences.user.login = ?#{principal.username}")
    List<Preferences> findByUserIsCurrentUser();

    @Query("select distinct preferences from Preferences preferences left join fetch preferences.movieRecomendations left join fetch preferences.serieRecomendations left join fetch preferences.requests")
    List<Preferences> findAllWithEagerRelationships();

    @Query("select preferences from Preferences preferences left join fetch preferences.movieRecomendations left join fetch preferences.serieRecomendations left join fetch preferences.requests where preferences.id =:id")
    Preferences findOneWithEagerRelationships(@Param("id") Long id);

    List<Preferences> findByName(String name);

    List<Preferences> findByNameAndUser(String name, User user);


}
