package com.example.demo.controller;

import com.example.demo.dto.postdto.PostViewDTO;
import com.example.demo.service.PostProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostProxyController {

    private final PostProxyService postProxyService;

    @GetMapping("/feed")
    public ResponseEntity<List<PostViewDTO>> getUserFeed(@RequestHeader("Authorization") String token) {
        List<PostViewDTO> posts = postProxyService.getVisiblePosts(token);
        return ResponseEntity.ok(posts);
    }
}
