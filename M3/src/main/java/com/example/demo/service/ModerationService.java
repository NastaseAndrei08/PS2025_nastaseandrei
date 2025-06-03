package com.example.demo.service;

import com.example.demo.dto.moderationdto.BlockUserDTO;
import com.example.demo.entity.ModerationAction;
import com.example.demo.entity.ModerationAction.ActionType;
import com.example.demo.entity.User;
import com.example.demo.repository.ModerationActionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ModerationActionRepository moderationActionRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;



    public void blockUser(Long moderatorId, BlockUserDTO dto) {
        ModerationAction action = ModerationAction.builder()
                .moderatorId(moderatorId)
                .userId(dto.getUserId())
                .reason(dto.getReason())
                .timestamp(LocalDateTime.now())
                .actionType(ActionType.BLOCK_USER)
                .build();

        moderationActionRepository.save(action);
    }

    public boolean isUserBlocked(String email) {
        Optional<User> userOpt = userRepository.findUserByEmail(email);
        if (userOpt.isEmpty()) return false;

        Long userId = userOpt.get().getId();
        return moderationActionRepository.existsByUserIdAndActionType(userId, ActionType.BLOCK_USER);
    }

    public void unblockUser(Long moderatorId, Long userId) {
        moderationActionRepository.deleteByUserIdAndActionType(userId, ModerationAction.ActionType.BLOCK_USER);
    }

    public void deletePost(Long postId, Long moderatorId) {
        moderationActionRepository.save(
                ModerationAction.builder()
                        .moderatorId(moderatorId)
                        .userId(null)
                        .timestamp(LocalDateTime.now())
                        .actionType(ModerationAction.ActionType.DELETE_POST)
                        .reason("Deleted via moderation")
                        .build()
        );

        String url = "http://localhost:8082/api/posts/moderate/" + postId;
        restTemplate.delete(url);
    }

    public void deleteComment(Long commentId, Long moderatorId) {
        moderationActionRepository.save(
                ModerationAction.builder()
                        .moderatorId(moderatorId)
                        .userId(null)
                        .timestamp(LocalDateTime.now())
                        .actionType(ModerationAction.ActionType.DELETE_COMMENT)
                        .reason("Deleted via moderation")
                        .build()
        );

        String url = "http://localhost:8082/api/comments/moderate/" + commentId;
        restTemplate.delete(url);
    }


}
