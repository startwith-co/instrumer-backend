package instrumers.backend.solution.plan.detail.repository;

import instrumers.backend.solution.plan.detail.model.SolutionPlanDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionPlanDetailJpaRepository extends JpaRepository<SolutionPlanDetailEntity, Long> {
}
