package com.example.demo.service;

import com.example.demo.dto.userdto.FriendRequestViewDTO;
import com.example.demo.dto.userdto.UserViewDTO;
import com.example.demo.dto.userdto.UserViewFriendDTO;
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
        System.out.println("Sender email primit din token: " + senderEmail);

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

    public List<FriendRequestViewDTO> getPendingRequests(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return friendRequestRepository.findByReceiverAndStatus(user, FriendRequestStatus.PENDING)
                .stream()
                .map(req -> new FriendRequestViewDTO(
                        req.getId(),
                        req.getSender().getEmail(),
                        req.getSender().getName(),
                        req.getReceiver().getEmail(),
                        req.getStatus(),
                        req.getTimestamp()
                ))
                .toList();
    }

    public List<UserViewFriendDTO> getFriends(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FriendRequest> accepted = friendRequestRepository
                .findBySenderOrReceiverAndStatus(user, user, FriendRequestStatus.ACCEPTED);

        return accepted.stream()
                .map(req -> {
                    User friend = req.getSender().equals(user) ? req.getReceiver() : req.getSender();
                    return new UserViewFriendDTO(friend.getId(), friend.getName(), friend.getEmail());
                })
                .toList();
    }

    public List<String> getAcceptedFriendsEmails(String userEmail) {
        return getFriends(userEmail).stream()
                .map(friend -> friend.getEmail())
                .toList();
    }




}
