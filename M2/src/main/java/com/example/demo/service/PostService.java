package com.example.demo.service;

import com.example.demo.dto.postdto.PostCreateDTO;
import com.example.demo.dto.postdto.PostViewDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.HashtagRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final FriendshipService friendshipService; // ai nevoie de prietenii!

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

    public List<PostViewDTO> getVisiblePosts(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> friends = friendshipService.getFriendsAsEntities(user);
        List<Post> allPosts = new ArrayList<>();

        allPosts.addAll(postRepository.findByAuthor(user)); // proprii
        allPosts.addAll(postRepository.findByVisibility(PostVisibility.PUBLIC)); // publice
        allPosts.addAll(postRepository.findByAuthorInAndVisibility(friends, PostVisibility.FRIENDS)); // prieteni

        return allPosts.stream().distinct().map(this::mapToViewDTO).toList();
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
}
