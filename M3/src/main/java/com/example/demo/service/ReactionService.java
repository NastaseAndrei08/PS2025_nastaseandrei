package com.example.demo.service;

import com.example.demo.dto.reactiondto.ReactionDTO;
import com.example.demo.entity.Reaction;
import com.example.demo.enums.ReactionType;
import com.example.demo.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;

    public Reaction addOrUpdateReaction(ReactionDTO dto) {
        if (dto.getPostId() != null) {
            return reactionRepository.findByUserIdAndPostId(dto.getUserId(), dto.getPostId())
                    .map(existing -> {
                        existing.setType(dto.getType());
                        return reactionRepository.save(existing);
                    })
                    .orElseGet(() -> reactionRepository.save(toEntity(dto)));
        } else if (dto.getCommentId() != null) {
            return reactionRepository.findByUserIdAndCommentId(dto.getUserId(), dto.getCommentId())
                    .map(existing -> {
                        existing.setType(dto.getType());
                        return reactionRepository.save(existing);
                    })
                    .orElseGet(() -> reactionRepository.save(toEntity(dto)));
        } else {
            throw new IllegalArgumentException("Either postId or commentId must be provided.");
        }
    }

    public List<Reaction> getReactionsForPost(Long postId) {
        return reactionRepository.findAllByPostId(postId);
    }

    public List<Reaction> getReactionsForComment(Long commentId) {
        return reactionRepository.findAllByCommentId(commentId);
    }

    private Reaction toEntity(ReactionDTO dto) {
        return Reaction.builder()
                .type(dto.getType())
                .userId(dto.getUserId())
                .postId(dto.getPostId())
                .commentId(dto.getCommentId())
                .build();
    }

    public Map<ReactionType, Long> getReactionCountsForPost(Long postId) {
        return reactionRepository.findAllByPostId(postId).stream()
                .collect(Collectors.groupingBy(Reaction::getType, Collectors.counting()));
    }

    public Map<ReactionType, Long> getReactionCountsForComment(Long commentId) {
        return reactionRepository.findAllByCommentId(commentId).stream()
                .collect(Collectors.groupingBy(Reaction::getType, Collectors.counting()));
    }


}
