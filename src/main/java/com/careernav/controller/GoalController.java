package com.careernav.controller;

import com.careernav.model.Goal;
import com.careernav.model.User;
import com.careernav.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalRepository goalRepository;

    @GetMapping
    public List<Goal> getAll(@AuthenticationPrincipal User user) {
        return goalRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user, @RequestBody Goal goal) {
        goal.setUser(user);
        return ResponseEntity.ok(goalRepository.save(goal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user,
                                    @PathVariable Long id, @RequestBody Goal updated) {
        return goalRepository.findById(id)
            .filter(g -> g.getUser().getId().equals(user.getId()))
            .map(g -> {
                if (updated.getTitle() != null) g.setTitle(updated.getTitle());
                if (updated.getCategory() != null) g.setCategory(updated.getCategory());
                if (updated.getDeadline() != null) g.setDeadline(updated.getDeadline());
                if (updated.getStatus() != null) g.setStatus(updated.getStatus());
                return ResponseEntity.ok(goalRepository.save(g));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return goalRepository.findById(id)
            .filter(g -> g.getUser().getId().equals(user.getId()))
            .map(g -> { goalRepository.delete(g); return ResponseEntity.ok(Map.of("message", "Deleted.")); })
            .orElse(ResponseEntity.notFound().build());
    }
}
