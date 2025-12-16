package instrumers.backend.solution.plan.detail.repository;

import instrumers.backend.solution.plan.detail.model.SolutionPlanDetailEntity;
import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionPlanDetailJpaRepository extends JpaRepository<SolutionPlanDetailEntity, Long> {
    List<SolutionPlanDetailEntity> findAllBySolutionPlanEntity(SolutionPlanEntity solutionPlanEntity);
}
