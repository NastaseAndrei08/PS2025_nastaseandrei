package com.example.demo.dto.userdto;

import com.example.demo.entity.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestViewDTO {
    private Long id;
    private String senderEmail;
    private String senderName;
    private String receiverEmail;
    private FriendRequestStatus status;
    private LocalDateTime timestamp;
}
