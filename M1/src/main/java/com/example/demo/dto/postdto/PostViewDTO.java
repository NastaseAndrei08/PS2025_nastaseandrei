package com.example.demo.dto.postdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostViewDTO {

    private Long id;
    private String content;
    private String imageUrl;
    private String visibility; // PUBLIC or FRIENDS
    private String authorName;
    private String authorEmail;
    private LocalDateTime timestamp;
    private List<String> hashtags;
}
