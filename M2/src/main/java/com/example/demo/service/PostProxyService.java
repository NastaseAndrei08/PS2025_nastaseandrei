package com.example.demo.service;

import com.example.demo.dto.PostViewDTO;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostProxyService {

    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private final FriendRequestService friendRequestService;

    public List<PostViewDTO> getVisiblePosts(String token) {
        String email = jwtService.extractUsername(token.substring(7));
        List<String> friendEmails = friendRequestService.getAcceptedFriendsEmails(email);

        // Call M2
        ResponseEntity<PostViewDTO[]> response = restTemplate.getForEntity(
                "http://localhost:8082/api/posts/all",  // M2 endpoint
                PostViewDTO[].class
        );

        List<PostViewDTO> allPosts = Arrays.asList(response.getBody());

        // Filter
        return allPosts.stream()
                .filter(post ->
                        post.getAuthorEmail().equals(email) ||
                                post.getVisibility().equalsIgnoreCase("PUBLIC") ||
                                (post.getVisibility().equalsIgnoreCase("FRIENDS") &&
                                        friendEmails.contains(post.getAuthorEmail()))
                )
                .toList();
    }
}
