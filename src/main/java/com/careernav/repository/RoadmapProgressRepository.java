package com.careernav.repository;
import com.careernav.model.RoadmapProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface RoadmapProgressRepository extends JpaRepository<RoadmapProgress, Long> {
    List<RoadmapProgress> findByUserIdAndRoadmapId(Long userId, String roadmapId);
    Optional<RoadmapProgress> findByUserIdAndRoadmapIdAndStepId(Long userId, String roadmapId, String stepId);
}
