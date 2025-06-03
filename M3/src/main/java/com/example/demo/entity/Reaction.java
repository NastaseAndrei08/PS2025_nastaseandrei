package com.example.demo.entity;

import com.example.demo.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType type;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Long postId;

    @Column
    private Long commentId;
}
