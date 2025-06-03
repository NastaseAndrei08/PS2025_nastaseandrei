package com.example.demo.dto.postdto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostWithCommentsResponseDTO {
    private PostResponseDTO post;
    private List<CommentResponseDTO> comments;
}
