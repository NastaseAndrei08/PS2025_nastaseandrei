package com.example.demo.controller;

import com.example.demo.dto.userdto.FriendRequestViewDTO;
import com.example.demo.dto.userdto.UserViewFriendDTO;
import com.example.demo.entity.FriendRequest;
import com.example.demo.entity.User;
import com.example.demo.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping("/send")
    public ResponseEntity<String> sendRequest(@RequestParam String to, Principal principal) {
        friendRequestService.sendRequest(principal.getName(), to);
        return ResponseEntity.ok("Friend request sent to " + to);
    }

    @PostMapping("/respond")
    public ResponseEntity<String> respond(@RequestParam Long requestId, @RequestParam boolean accepted) {
        friendRequestService.respondToRequest(requestId, accepted);
        return ResponseEntity.ok("Friend request " + (accepted ? "accepted" : "rejected"));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestViewDTO>> getPending(Principal principal) {
        return ResponseEntity.ok(friendRequestService.getPendingRequests(principal.getName()));
    }


    @GetMapping("/list")
    public ResponseEntity<List<UserViewFriendDTO>> getFriends(Principal principal) {
        return ResponseEntity.ok(friendRequestService.getFriends(principal.getName()));
    }

}
