package com.example.demo.dto.postdto;

import com.example.demo.entity.PostVisibility;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostViewDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private PostVisibility visibility;
    private String authorName;
    private String authorEmail;
    private LocalDateTime timestamp;
    private List<String> hashtags;
}

