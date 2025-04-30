package com.example.demo.controller;

import com.example.demo.dto.postdto.PostCreateDTO;
import com.example.demo.dto.postdto.PostViewDTO;
import com.example.demo.service.AuthService;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthService authService; // adaugă acest serviciu

    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostCreateDTO dto) {

        String token = authHeader.substring(7); // elimină "Bearer "
        String email = authService.extractEmailFromToken(token); // cere emailul de la M1

        postService.createPost(email, dto);
        return ResponseEntity.ok("Post created successfully");
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostViewDTO>> getVisiblePosts(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = authService.extractEmailFromToken(token);

        return ResponseEntity.ok(postService.getVisiblePosts(email));
    }
}
