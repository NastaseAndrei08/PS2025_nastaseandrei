package com.example.demo.dto.postdto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDTO {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
