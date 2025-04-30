package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendshipService {

    public List<User> getFriendsAsEntities(User user) {
        // Pentru test, returnează o listă goală sau useri hardcodați
        return List.of();
    }
}
