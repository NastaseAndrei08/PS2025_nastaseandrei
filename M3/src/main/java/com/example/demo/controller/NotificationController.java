package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/{userId}")
    public List<Notification> getUnreadNotifications(@PathVariable Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    @PostMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
}
