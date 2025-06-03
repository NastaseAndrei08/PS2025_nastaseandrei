package com.example.demo.service;

import com.example.demo.dto.postdto.PostCreateDTO;
import com.example.demo.dto.postdto.PostViewDTO;
import com.example.demo.dto.postdto.PostWithCommentsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostProxyService {

    private final RestTemplate restTemplate;

    public ResponseEntity<String> createPost(String token, PostCreateDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<PostCreateDTO> request = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
                "http://localhost:8082/api/posts/create",
                HttpMethod.POST,
                request,
                String.class
        );
    }

    public ResponseEntity<List<PostViewDTO>> getAllPosts(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PostViewDTO[]> response = restTemplate.exchange(
                "http://localhost:8082/api/posts/feed",
                HttpMethod.GET,
                request,
                PostViewDTO[].class
        );

        return ResponseEntity.ok(Arrays.asList(response.getBody()));
    }

    public ResponseEntity<String> updatePost(Long postId, String token, PostCreateDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<PostCreateDTO> request = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
                "http://localhost:8082/api/posts/update/" + postId,
                HttpMethod.PUT,
                request,
                String.class
        );
    }

    public ResponseEntity<String> deletePost(Long postId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                "http://localhost:8082/api/posts/delete/" + postId,
                HttpMethod.DELETE,
                request,
                String.class
        );
    }

    public PostWithCommentsDTO getPostWithComments(Long postId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<PostWithCommentsDTO> response = restTemplate.exchange(
                    "http://localhost:8082/api/posts/with-comments/" + postId,
                    HttpMethod.GET,
                    request,
                    PostWithCommentsDTO.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("❌ 403 Response Body: " + e.getResponseBodyAsString());
            throw e;
        }
    }






}
