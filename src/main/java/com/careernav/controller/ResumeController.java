package com.careernav.controller;

import com.careernav.model.ResumeData;
import com.careernav.model.User;
import com.careernav.repository.ResumeDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeDataRepository resumeDataRepository;

    @GetMapping
    public ResponseEntity<?> get(@AuthenticationPrincipal User user) {
        return resumeDataRepository.findByUserId(user.getId())
            .map(r -> ResponseEntity.ok(Map.of("data", r.getData() != null ? r.getData() : "{}")))
            .orElse(ResponseEntity.ok(Map.of("data", "{}")));
    }

    @PutMapping
    public ResponseEntity<?> save(@AuthenticationPrincipal User user,
                                  @RequestBody Map<String, String> req) {
        String data = req.get("data");
        if (data == null) return ResponseEntity.badRequest().body(Map.of("error", "data field required."));

        ResumeData resume = resumeDataRepository.findByUserId(user.getId())
            .orElse(new ResumeData());
        resume.setUser(user);
        resume.setData(data);
        resumeDataRepository.save(resume);
        return ResponseEntity.ok(Map.of("message", "Resume saved."));
    }
}
