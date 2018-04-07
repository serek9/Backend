package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.Request;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Request entity.
 */
@SuppressWarnings("unused")
public interface RequestRepository extends JpaRepository<Request,Long> {

    @Query("select request from Request request where request.userRequester.login = ?#{principal.username}")
    List<Request> findByUserRequesterIsCurrentUser();

    @Query("select distinct request from Request request left join fetch request.userGuests")
    List<Request> findAllWithEagerRelationships();

    @Query("select request from Request request left join fetch request.userGuests where request.id =:id")
    Request findOneWithEagerRelationships(@Param("id") Long id);

}
