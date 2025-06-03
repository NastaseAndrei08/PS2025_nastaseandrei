package com.example.demo.controller;

import com.example.demo.dto.moderationdto.BlockUserDTO;
import com.example.demo.service.ModerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/moderate")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;

    @PostMapping("/block-user")
    public ResponseEntity<String> blockUser(@RequestBody BlockUserDTO dto) {
        moderationService.blockUser(dto.getModeratorId(), dto);
        return ResponseEntity.ok("User blocked and action recorded.");
    }

    @GetMapping("/is-blocked")
    public ResponseEntity<Boolean> isUserBlocked(@RequestParam String email) {
        boolean isBlocked = moderationService.isUserBlocked(email);
        return ResponseEntity.ok(isBlocked);
    }

    @DeleteMapping("/unblock-user")
    public ResponseEntity<String> unblockUser(@RequestParam Long userId, @RequestParam Long moderatorId) {
        moderationService.unblockUser(moderatorId, userId);
        return ResponseEntity.ok("User has been unblocked.");
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<String> deletePost(
            @RequestHeader("Moderator-Id") Long moderatorId,
            @PathVariable Long postId
    ) {
        moderationService.deletePost(postId, moderatorId);
        return ResponseEntity.ok("Post deleted by moderator.");
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @RequestHeader("Moderator-Id") Long moderatorId,
            @PathVariable Long commentId
    ) {
        moderationService.deleteComment(commentId, moderatorId);
        return ResponseEntity.ok("Comment deleted by moderator.");
    }



}
