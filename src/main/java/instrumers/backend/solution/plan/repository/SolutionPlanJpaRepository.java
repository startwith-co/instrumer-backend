package instrumers.backend.solution.plan.repository;

import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionPlanJpaRepository extends JpaRepository<SolutionPlanEntity, Integer> {
    List<SolutionPlanEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
