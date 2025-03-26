package com.example.demo.service;

import com.example.demo.entity.FriendRequest;
import com.example.demo.entity.FriendRequestStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.FriendRequestRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public void sendRequest(String senderEmail, String receiverEmail) {
        User sender = userRepository.findUserByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findUserByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            throw new RuntimeException("Friend request already exists");
        }

        FriendRequest request = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();

        friendRequestRepository.save(request);
    }

    public void respondToRequest(Long requestId, boolean accepted) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        request.setStatus(accepted ? FriendRequestStatus.ACCEPTED : FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }

    public List<FriendRequest> getPendingRequests(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);
    }

    public List<User> getFriends(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FriendRequest> accepted = friendRequestRepository
                .findBySenderOrReceiverAndStatus(user, user, FriendRequestStatus.ACCEPTED);

        return accepted.stream()
                .map(req -> req.getSender().equals(user) ? req.getReceiver() : req.getSender())
                .toList();
    }
}
