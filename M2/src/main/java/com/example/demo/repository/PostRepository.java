package com.example.demo.repository;

import com.example.demo.entity.Post;
import com.example.demo.entity.PostVisibility;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Postări proprii
    List<Post> findByAuthor(User author);

    // Postări publice
    List<Post> findByVisibility(PostVisibility visibility);

    // Postări de la anumiți autori cu vizibilitate FRIENDS
    List<Post> findByAuthorInAndVisibility(List<User> authors, PostVisibility visibility);
}

