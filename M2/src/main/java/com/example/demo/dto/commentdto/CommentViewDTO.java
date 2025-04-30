package com.example.demo.dto.commentdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentViewDTO {
    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
}
