package com.careernav.repository;
import com.careernav.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByUserId(Long userId);
}
