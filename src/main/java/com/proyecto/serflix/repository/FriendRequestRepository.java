package com.proyecto.serflix.repository;

import com.proyecto.serflix.domain.FriendRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FriendRequest entity.
 */
@SuppressWarnings("unused")
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {

    @Query("select friendRequest from FriendRequest friendRequest where friendRequest.sender.login = ?#{principal.username}")
    List<FriendRequest> findBySenderIsCurrentUser();

    @Query("select friendRequest from FriendRequest friendRequest where friendRequest.receiver.login = ?#{principal.username}")
    List<FriendRequest> findByReceiverIsCurrentUser();

}
