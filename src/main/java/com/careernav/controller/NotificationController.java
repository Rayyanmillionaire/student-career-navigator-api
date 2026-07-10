package com.careernav.controller;

import com.careernav.model.Notification;
import com.careernav.model.User;
import com.careernav.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping
    public List<Notification> getAll(@AuthenticationPrincipal User user) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> unreadCount(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("count",
                notificationRepository.countByUserIdAndReadFalse(user.getId())));
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<?> markAllRead(@AuthenticationPrincipal User user) {
        List<Notification> notifs = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        notifs.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifs);
        return ResponseEntity.ok(Map.of("message", "All marked as read."));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markRead(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return notificationRepository.findById(id)
            .filter(n -> n.getUser().getId().equals(user.getId()))
            .map(n -> { n.setRead(true); return ResponseEntity.ok(notificationRepository.save(n)); })
            .orElse(ResponseEntity.notFound().build());
    }
}
