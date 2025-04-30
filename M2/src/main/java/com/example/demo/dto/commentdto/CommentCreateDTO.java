package com.example.demo.dto.commentdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDTO {
    private Long postId;
    private String content;
}
