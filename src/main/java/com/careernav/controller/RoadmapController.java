package com.careernav.controller;

import com.careernav.model.RoadmapProgress;
import com.careernav.model.User;
import com.careernav.repository.RoadmapProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roadmaps")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapProgressRepository roadmapProgressRepository;

    @GetMapping("/{roadmapId}/progress")
    public ResponseEntity<?> getProgress(@AuthenticationPrincipal User user,
                                         @PathVariable String roadmapId) {
        List<RoadmapProgress> progress = roadmapProgressRepository
                .findByUserIdAndRoadmapId(user.getId(), roadmapId);
        List<String> completedSteps = progress.stream()
                .map(RoadmapProgress::getStepId)
                .toList();
        return ResponseEntity.ok(Map.of("roadmapId", roadmapId, "completed", completedSteps));
    }

    @PostMapping("/{roadmapId}/progress")
    public ResponseEntity<?> toggleStep(@AuthenticationPrincipal User user,
                                        @PathVariable String roadmapId,
                                        @RequestBody Map<String, String> req) {
        String stepId = req.get("stepId");
        if (stepId == null) return ResponseEntity.badRequest().body(Map.of("error", "stepId required."));

        var existing = roadmapProgressRepository
                .findByUserIdAndRoadmapIdAndStepId(user.getId(), roadmapId, stepId);

        if (existing.isPresent()) {
            roadmapProgressRepository.delete(existing.get());
            return ResponseEntity.ok(Map.of("action", "removed", "stepId", stepId));
        } else {
            RoadmapProgress p = new RoadmapProgress();
            p.setUser(user);
            p.setRoadmapId(roadmapId);
            p.setStepId(stepId);
            roadmapProgressRepository.save(p);
            return ResponseEntity.ok(Map.of("action", "added", "stepId", stepId));
        }
    }
}
