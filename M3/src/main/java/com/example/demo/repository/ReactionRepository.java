package com.example.demo.repository;

import com.example.demo.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserIdAndPostId(Long userId, Long postId);
    Optional<Reaction> findByUserIdAndCommentId(Long userId, Long commentId);
    List<Reaction> findAllByPostId(Long postId);
    List<Reaction> findAllByCommentId(Long commentId);
}
