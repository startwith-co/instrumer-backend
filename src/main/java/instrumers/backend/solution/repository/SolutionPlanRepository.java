package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionPlanEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionPlanRepository extends JpaRepository<SolutionPlanEntity, Integer> {
    List<SolutionPlanEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
