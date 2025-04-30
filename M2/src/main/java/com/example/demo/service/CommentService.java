package com.example.demo.service;

import com.example.demo.dto.commentdto.CommentCreateDTO;
import com.example.demo.dto.commentdto.CommentViewDTO;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createComment(String email, CommentCreateDTO dto) {
        User author = userRepository.findUserByEmail(email).orElseThrow();
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();

        Comment comment = new Comment(dto.getContent(), author, post);
        commentRepository.save(comment);
    }

    public List<CommentViewDTO> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPost(post).stream()
                .map(c -> new CommentViewDTO(
                        c.getId(),
                        c.getContent(),
                        c.getAuthor().getName(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own comment.");
        }
        commentRepository.delete(comment);
    }

    public void editComment(Long commentId, String newContent, String email) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!comment.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("You can only edit your own comment.");
        }
        comment.setContent(newContent);
        commentRepository.save(comment);
    }
}
