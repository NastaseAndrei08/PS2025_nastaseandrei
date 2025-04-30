package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;

    public String extractEmailFromToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(token, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8081/api/auth/validate",
                request,
                String.class
        );

        return response.getBody(); // emailul extras
    }

    public List<String> getFriendsEmails(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                "http://localhost:8081/api/friends/emails",
                HttpMethod.GET,
                request,
                List.class
        );

        return response.getBody();
    }

}
