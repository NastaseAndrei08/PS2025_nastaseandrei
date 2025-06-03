package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostGatewayController {

    private final RestTemplate restTemplate;

    @Value("${m2.url}")
    private String m2Url;

    @GetMapping("/{postId}/with-comments")
    public ResponseEntity<?> getPostWithComments(@PathVariable Long postId) {
        String url = m2Url + "/api/posts/" + postId + "/with-comments";

        ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
