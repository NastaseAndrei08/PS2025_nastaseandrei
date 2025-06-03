package com.example.demo.dto.postdto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithCommentsDTO {
    private Map<String, Object> post;
    private List<Map<String, Object>> comments;
}
