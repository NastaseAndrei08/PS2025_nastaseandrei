package com.example.demo.controller;

import com.example.demo.dto.postdto.PostCreateDTO;
import com.example.demo.dto.postdto.PostViewDTO;
import com.example.demo.dto.postdto.PostWithCommentsDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthService authService;
    private final CommentService commentService;
    private final RestTemplate restTemplate;
    private final PostRepository postRepository;

    @Value("${m3.reactions.top-comments-url}")
    private String topCommentsUrl;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostCreateDTO dto) {

        String token = authHeader.substring(7); // elimină "Bearer "
        String email = authService.extractEmailFromToken(token);

        postService.createPost(email, dto);
        return ResponseEntity.ok("Post created successfully");
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostViewDTO>> getVisiblePosts(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = authService.extractEmailFromToken(token);

        return ResponseEntity.ok(postService.getVisiblePosts(email, token));
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long postId,
            @RequestBody PostCreateDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = authService.extractEmailFromToken(token);

        return postService.updatePost(postId, email, dto);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = authService.extractEmailFromToken(token);

        return postService.deletePost(postId, email);
    }


    @DeleteMapping("/moderate/{postId}")
    public ResponseEntity<String> deleteByModerator(@PathVariable Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = optionalPost.get();

        post.getHashtags().clear();
        postRepository.save(post); // Salvează fără hashtag-uri

        postRepository.deleteById(postId);

        return ResponseEntity.ok("Post deleted by moderator.");
    }


}
