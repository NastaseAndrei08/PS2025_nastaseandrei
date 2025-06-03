package com.example.demo.controller;

import com.example.demo.dto.reactiondto.ReactionDTO;
import com.example.demo.entity.Reaction;
import com.example.demo.enums.ReactionType;
import com.example.demo.repository.ReactionRepository;
import com.example.demo.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;
    private final ReactionRepository reactionRepository;

    @PostMapping
    public ResponseEntity<Reaction> react(@RequestBody ReactionDTO dto) {
        return ResponseEntity.ok(reactionService.addOrUpdateReaction(dto));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Reaction>> getReactionsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(reactionService.getReactionsForPost(postId));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<List<Reaction>> getReactionsForComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(reactionService.getReactionsForComment(commentId));
    }

    @GetMapping("/count/post/{postId}")
    public ResponseEntity<Map<ReactionType, Long>> getReactionCountsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(reactionService.getReactionCountsForPost(postId));
    }

    @GetMapping("/count/comment/{commentId}")
    public ResponseEntity<Map<ReactionType, Long>> getReactionCountsForComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(reactionService.getReactionCountsForComment(commentId));
    }

    @GetMapping("/top-comments")
    public ResponseEntity<List<Long>> getTopCommentIdsByReactions() {
        List<Reaction> allReactions = reactionRepository.findAll();

        Map<Long, Long> commentScoreMap = allReactions.stream()
                .filter(r -> r.getCommentId() != null)
                .collect(Collectors.groupingBy(
                        Reaction::getCommentId,
                        Collectors.counting()
                ));

        List<Long> sortedCommentIds = commentScoreMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sortedCommentIds);
    }

    @GetMapping("/top-comments/post/{postId}")
    public ResponseEntity<List<Long>> getTopCommentsByPostId(@PathVariable Long postId) {
        List<Reaction> allReactions = reactionRepository.findAll();

        Map<Long, Long> commentScoreMap = allReactions.stream()
                .filter(r -> r.getCommentId() != null && r.getPostId() != null)
                .filter(r -> r.getPostId().equals(postId))
                .collect(Collectors.groupingBy(
                        Reaction::getCommentId,
                        Collectors.counting()
                ));

        List<Long> sortedCommentIds = commentScoreMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sortedCommentIds);
    }
}
