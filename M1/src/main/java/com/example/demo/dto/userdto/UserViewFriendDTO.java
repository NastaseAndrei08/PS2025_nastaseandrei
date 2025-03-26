package com.example.demo.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserViewFriendDTO {
    private Long id;
    private String name;
    private String email;
}
