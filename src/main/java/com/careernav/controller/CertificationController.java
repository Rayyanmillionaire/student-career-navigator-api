package com.careernav.controller;

import com.careernav.model.Certification;
import com.careernav.model.User;
import com.careernav.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationRepository certificationRepository;

    @GetMapping
    public List<Certification> getAll(@AuthenticationPrincipal User user) {
        return certificationRepository.findByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user, @RequestBody Certification cert) {
        cert.setUser(user);
        return ResponseEntity.ok(certificationRepository.save(cert));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user,
                                    @PathVariable Long id, @RequestBody Certification updated) {
        return certificationRepository.findById(id)
            .filter(c -> c.getUser().getId().equals(user.getId()))
            .map(c -> {
                if (updated.getName() != null) c.setName(updated.getName());
                if (updated.getIssuer() != null) c.setIssuer(updated.getIssuer());
                if (updated.getStatus() != null) c.setStatus(updated.getStatus());
                if (updated.getDate() != null) c.setDate(updated.getDate());
                if (updated.getUrl() != null) c.setUrl(updated.getUrl());
                return ResponseEntity.ok(certificationRepository.save(c));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return certificationRepository.findById(id)
            .filter(c -> c.getUser().getId().equals(user.getId()))
            .map(c -> { certificationRepository.delete(c); return ResponseEntity.ok(Map.of("message", "Deleted.")); })
            .orElse(ResponseEntity.notFound().build());
    }
}
