package com.example.demo.dto.postdto;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithCommentsDTO {
    private Post post;
    private List<Comment> comments;
}
