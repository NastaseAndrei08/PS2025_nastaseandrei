package com.example.demo.dto.reactiondto;

import com.example.demo.enums.ReactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionDTO {
    private ReactionType type;
    private Long userId;
    private Long postId;
    private Long commentId;
}
