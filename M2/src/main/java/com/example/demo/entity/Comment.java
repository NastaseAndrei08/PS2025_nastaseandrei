package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    private User author;

    @ManyToOne
    @JsonIgnore
    private Post post;

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String content, User author, Post post) {
        this.content = content;
        this.author = author;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }

    // Getteri și setteri
    public Long getId() { return id; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }

    public Post getPost() { return post; }

    public void setPost(Post post) { this.post = post; }
}
