package com.example.demo.controller;

import com.example.demo.dto.commentdto.CommentCreateDTO;
import com.example.demo.dto.commentdto.CommentViewDTO;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;
    private final CommentRepository commentRepository;

    @PostMapping
    public ResponseEntity<String> createComment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CommentCreateDTO dto) {

        String token = authHeader.substring(7);
        String email = authService.extractEmailFromToken(token);
        commentService.createComment(email, dto);
        return ResponseEntity.ok("Comment created.");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentViewDTO>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<String> editComment(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long commentId,
            @RequestBody String newContent) {

        String token = authHeader.substring(7);
        String email = authService.extractEmailFromToken(token);
        commentService.editComment(commentId, newContent, email);
        return ResponseEntity.ok("Comment edited.");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long commentId) {

        String token = authHeader.substring(7);
        String email = authService.extractEmailFromToken(token);
        commentService.deleteComment(commentId, email);
        return ResponseEntity.ok("Comment deleted.");
    }

    @DeleteMapping("/moderate/{commentId}")
    public ResponseEntity<String> deleteCommentByModerator(@PathVariable Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        commentRepository.deleteById(commentId);
        return ResponseEntity.ok("Comment deleted by moderator.");
    }

}
