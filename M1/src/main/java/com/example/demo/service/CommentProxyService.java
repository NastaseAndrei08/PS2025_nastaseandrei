package com.example.demo.service;

import com.example.demo.dto.commentdto.CommentCreateDTO;
import com.example.demo.dto.commentdto.CommentViewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CommentProxyService {

    @Autowired
    private RestTemplate restTemplate;

    public List<CommentViewDTO> getCommentsByPostId(Long postId) {
        String url = "http://localhost:8082/api/comments/" + postId;
        ResponseEntity<CommentViewDTO[]> response = restTemplate.getForEntity(url, CommentViewDTO[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public ResponseEntity<String> createComment(String token, CommentCreateDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CommentCreateDTO> request = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
                "http://localhost:8082/api/comments",
                HttpMethod.POST,
                request,
                String.class
        );
    }

    public ResponseEntity<String> editComment(Long commentId, String token, String newContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(newContent, headers);

        return restTemplate.exchange(
                "http://localhost:8082/api/comments/" + commentId,
                HttpMethod.PUT,
                request,
                String.class
        );
    }

    public ResponseEntity<String> deleteComment(Long commentId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:8082/api/comments/" + commentId,
                HttpMethod.DELETE,
                request,
                String.class
        );
    }


}
