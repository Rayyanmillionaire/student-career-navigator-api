package com.careernav.controller;

import com.careernav.model.Internship;
import com.careernav.model.User;
import com.careernav.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internships")
@RequiredArgsConstructor
public class InternshipController {

    private final InternshipRepository internshipRepository;

    @GetMapping
    public List<Internship> getAll(@AuthenticationPrincipal User user) {
        return internshipRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user, @RequestBody Internship internship) {
        internship.setUser(user);
        return ResponseEntity.ok(internshipRepository.save(internship));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user,
                                    @PathVariable Long id, @RequestBody Internship updated) {
        return internshipRepository.findById(id)
            .filter(i -> i.getUser().getId().equals(user.getId()))
            .map(i -> {
                if (updated.getCompany() != null) i.setCompany(updated.getCompany());
                if (updated.getRole() != null) i.setRole(updated.getRole());
                if (updated.getStatus() != null) i.setStatus(updated.getStatus());
                if (updated.getDeadline() != null) i.setDeadline(updated.getDeadline());
                if (updated.getLink() != null) i.setLink(updated.getLink());
                return ResponseEntity.ok(internshipRepository.save(i));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return internshipRepository.findById(id)
            .filter(i -> i.getUser().getId().equals(user.getId()))
            .map(i -> { internshipRepository.delete(i); return ResponseEntity.ok(Map.of("message", "Deleted.")); })
            .orElse(ResponseEntity.notFound().build());
    }
}
