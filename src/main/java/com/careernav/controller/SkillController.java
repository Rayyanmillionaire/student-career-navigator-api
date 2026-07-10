package com.careernav.controller;

import com.careernav.model.Skill;
import com.careernav.model.User;
import com.careernav.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository skillRepository;

    @GetMapping
    public List<Skill> getAll(@AuthenticationPrincipal User user) {
        return skillRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user, @RequestBody Skill skill) {
        skill.setUser(user);
        return ResponseEntity.ok(skillRepository.save(skill));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user,
                                    @PathVariable Long id, @RequestBody Skill updated) {
        return skillRepository.findById(id)
            .filter(s -> s.getUser().getId().equals(user.getId()))
            .map(s -> {
                s.setName(updated.getName());
                s.setCategory(updated.getCategory());
                s.setLevel(updated.getLevel());
                s.setTags(updated.getTags());
                return ResponseEntity.ok(skillRepository.save(s));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return skillRepository.findById(id)
            .filter(s -> s.getUser().getId().equals(user.getId()))
            .map(s -> { skillRepository.delete(s); return ResponseEntity.ok(Map.of("message", "Deleted.")); })
            .orElse(ResponseEntity.notFound().build());
    }
}
