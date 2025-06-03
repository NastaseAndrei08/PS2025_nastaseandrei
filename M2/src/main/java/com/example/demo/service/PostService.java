package com.example.demo.service;

import com.example.demo.dto.postdto.PostCreateDTO;
import com.example.demo.dto.postdto.PostViewDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.HashtagRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final AuthService authService;

    public void createPost(String userEmail, PostCreateDTO dto) {
        User author = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Hashtag> hashtags = dto.getHashtags().stream()
                .map(this::getOrCreateHashtag)
                .toList();

        Post post = Post.builder()
                .author(author)
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .timestamp(LocalDateTime.now())
                .visibility(dto.getVisibility())
                .hashtags(hashtags)
                .build();

        postRepository.save(post);
    }

    public List<PostViewDTO> getVisiblePosts(String userEmail, String token) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Apelează M1 pentru a obține emailurile prietenilor
        List<String> friendEmails = authService.getFriendsEmails(token);

        // Găsește utilizatorii prieteni în baza de date locală M2
        List<User> friends = userRepository.findByEmailIn(friendEmails);

        // Adună toate postările vizibile
        List<Post> allPosts = new ArrayList<>();
        allPosts.addAll(postRepository.findByAuthor(user)); // postările proprii
        allPosts.addAll(postRepository.findByVisibility(PostVisibility.PUBLIC)); // postări publice
        allPosts.addAll(postRepository.findByAuthorInAndVisibility(friends, PostVisibility.FRIENDS)); // postări de la prieteni

        // Elimină duplicate, sortează descrescător după timestamp și mapează în DTO
        return allPosts.stream()
                .distinct()
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .map(this::mapToViewDTO)
                .toList();
    }


    private Hashtag getOrCreateHashtag(String name) {
        return hashtagRepository.findByName(name)
                .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(name).build()));
    }

    private PostViewDTO mapToViewDTO(Post post) {
        return new PostViewDTO(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                post.getVisibility(),
                post.getAuthor().getName(),
                post.getAuthor().getEmail(),
                post.getTimestamp(),
                post.getHashtags().stream().map(Hashtag::getName).toList()
        );
    }

    public ResponseEntity<String> updatePost(Long postId, String email, PostCreateDTO dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().getEmail().equals(email)) {
            return ResponseEntity.status(403).body("You can only edit your own posts.");
        }

        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        post.setVisibility(dto.getVisibility());
        post.setHashtags(new ArrayList<>(dto.getHashtags().stream()
                .map(this::getOrCreateHashtag)
                .toList()));

        postRepository.save(post);
        return ResponseEntity.ok("Post updated.");
    }

    @Transactional
    public ResponseEntity<String> deletePost(Long postId, String email) {
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().getEmail().equals(email)) {
            return ResponseEntity.status(403).body("You can only delete your own posts.");
        }

        post.setHashtags(new ArrayList<>());
        postRepository.save(post);
        postRepository.delete(post);

        return ResponseEntity.ok("Post deleted.");
    }

    public PostViewDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return mapToViewDTO(post);
    }

    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

}
