package com.example.demo.dto.postdto;


import com.example.demo.entity.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDTO {
    private String content;
    private String imageUrl;
    private PostVisibility visibility;
    private List<String> hashtags; // ex: ["java", "spring"]
}

