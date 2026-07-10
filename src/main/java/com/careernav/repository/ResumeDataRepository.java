package com.careernav.repository;
import com.careernav.model.ResumeData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ResumeDataRepository extends JpaRepository<ResumeData, Long> {
    Optional<ResumeData> findByUserId(Long userId);
}
