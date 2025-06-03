package com.example.demo.dto.moderationdto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockUserDTO {
    private Long userId;
    private String reason;
    private Long moderatorId;
}
