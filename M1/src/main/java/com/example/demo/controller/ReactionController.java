package com.example.demo.controller;

import com.example.demo.dto.reactiondto.ReactionDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final RestTemplate restTemplate;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${m3.url}")
    private String m3Url; // http://localhost:8083/api/reactions

    @PostMapping
    public ResponseEntity<?> sendReactionToM3(
            @RequestBody ReactionDTO dto,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtService.extractUsername(token);
        User user = userRepository.findUserByEmail(email).orElseThrow();
        dto.setUserId(user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReactionDTO> httpEntity = new HttpEntity<>(dto, headers);

        ResponseEntity<?> response = restTemplate.postForEntity(m3Url, httpEntity, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getReactionsForPost(@PathVariable Long postId) {
        String url = m3Url + "/post/" + postId;
        return restTemplate.getForEntity(url, Object.class);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getReactionsForComment(@PathVariable Long commentId) {
        String url = m3Url + "/comment/" + commentId;
        return restTemplate.getForEntity(url, Object.class);
    }

    @GetMapping("/count/post/{postId}")
    public ResponseEntity<?> getReactionCountsForPost(@PathVariable Long postId) {
        String url = m3Url + "/count/post/" + postId;
        return restTemplate.getForEntity(url, Map.class);
    }

    @GetMapping("/count/comment/{commentId}")
    public ResponseEntity<?> getReactionCountsForComment(@PathVariable Long commentId) {
        String url = m3Url + "/count/comment/" + commentId;
        return restTemplate.getForEntity(url, Map.class);
    }

    @GetMapping("/top-comments")
    public ResponseEntity<List<Integer>> getTopCommentIdsByReactions() {
        String url = m3Url + "/top-comments";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/top-comments/post/{postId}")
    public ResponseEntity<List<Long>> getTopCommentsByPostId(@PathVariable Long postId) {
        String url = m3Url + "/top-comments/post/" + postId;
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return ResponseEntity.ok(response.getBody());
    }
}
