package com.example.demo.repository;

import com.example.demo.entity.FriendRequest;
import com.example.demo.entity.User;
import com.example.demo.entity.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);

    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);

    List<FriendRequest> findBySenderOrReceiverAndStatus(User sender, User receiver, FriendRequestStatus status);
}
