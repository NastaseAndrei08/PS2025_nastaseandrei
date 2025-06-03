package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "moderation_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModerationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long moderatorId;
    private Long userId;
    private String reason;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    public enum ActionType {
        BLOCK_USER,
        DELETE_POST,
        DELETE_COMMENT
    }
}
