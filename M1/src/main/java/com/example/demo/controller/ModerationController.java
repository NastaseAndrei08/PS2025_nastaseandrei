package com.example.demo.controller;

import com.example.demo.dto.moderationdto.BlockUserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/moderate")
@RequiredArgsConstructor
public class ModerationController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${m3.url}")
    private String m3Url;

    @PostMapping("/block-user")
    public ResponseEntity<String> blockUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BlockUserDTO dto
    ) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        User moderator = optionalUser.get();
        if (moderator.getRole().getId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Not an admin/moderator");
        }

        dto.setModeratorId(moderator.getId());

        String url = "http://localhost:8083/moderate/block-user";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BlockUserDTO> request = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @DeleteMapping("/unblock-user")
    public ResponseEntity<String> unblockUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Long userId
    ) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        User moderator = userRepository.findUserByEmail(email).orElseThrow();

        if (moderator.getRole().getId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        String url = "http://localhost:8083/moderate/unblock-user?userId=" + userId + "&moderatorId=" + moderator.getId();
        restTemplate.delete(url);
        return ResponseEntity.ok("User has been unblocked.");
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<String> deletePost(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long postId
    ) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        User moderator = userRepository.findUserByEmail(email).orElseThrow();

        if (moderator.getRole().getId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        String url = "http://localhost:8083/moderate/delete-post/" + postId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Moderator-Id", String.valueOf(moderator.getId()));

        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        return ResponseEntity.ok("Post deleted successfully.");
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long commentId
    ) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        User moderator = userRepository.findUserByEmail(email).orElseThrow();

        if (moderator.getRole().getId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        String url = "http://localhost:8083/moderate/delete-comment/" + commentId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Moderator-Id", String.valueOf(moderator.getId()));

        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        return ResponseEntity.ok("Comment deleted.");
    }

}
