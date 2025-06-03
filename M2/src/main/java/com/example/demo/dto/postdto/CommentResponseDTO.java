package com.example.demo.dto.postdto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDTO {
    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdAt;
}
