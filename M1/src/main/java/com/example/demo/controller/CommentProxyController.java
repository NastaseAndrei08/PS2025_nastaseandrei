package com.example.demo.controller;

import com.example.demo.dto.commentdto.CommentCreateDTO;
import com.example.demo.dto.commentdto.CommentViewDTO;
import com.example.demo.service.CommentProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateway")
@RequiredArgsConstructor
public class CommentProxyController {

    private final CommentProxyService commentProxyService;

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<CommentViewDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentProxyService.getCommentsByPostId(postId));
    }

    @PostMapping("/comments")
    public ResponseEntity<String> createComment(@RequestHeader("Authorization") String token,
                                                @RequestBody CommentCreateDTO dto) {
        return commentProxyService.createComment(token, dto);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<String> editComment(@PathVariable Long commentId,
                                              @RequestHeader("Authorization") String token,
                                              @RequestBody String newContent) {
        return commentProxyService.editComment(commentId, token, newContent);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @RequestHeader("Authorization") String token) {
        return commentProxyService.deleteComment(commentId, token);
    }


}
