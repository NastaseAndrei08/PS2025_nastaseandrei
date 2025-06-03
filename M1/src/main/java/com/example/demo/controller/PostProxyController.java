package com.example.demo.controller;

import com.example.demo.dto.postdto.PostCreateDTO;
import com.example.demo.dto.postdto.PostViewDTO;
import com.example.demo.dto.postdto.PostWithCommentsDTO;
import com.example.demo.service.PostProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateway/posts")
@RequiredArgsConstructor
public class PostProxyController {

    private final PostProxyService postProxyService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String token,
                                             @RequestBody PostCreateDTO dto) {
        return postProxyService.createPost(token, dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostViewDTO>> getAllPosts(@RequestHeader("Authorization") String token) {
        return postProxyService.getAllPosts(token);
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId,
                                             @RequestHeader("Authorization") String token,
                                             @RequestBody PostCreateDTO dto) {
        return postProxyService.updatePost(postId, token, dto);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             @RequestHeader("Authorization") String token) {
        return postProxyService.deletePost(postId, token);
    }

    @GetMapping("/posts/with-comments/{postId}")
    public ResponseEntity<PostWithCommentsDTO> getPostWithComments(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(postProxyService.getPostWithComments(postId, token));
    }




}
