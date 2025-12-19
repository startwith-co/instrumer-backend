package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionPlanDetailEntity;
import instrumers.backend.solution.domain.SolutionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionPlanDetailJpaRepository extends JpaRepository<SolutionPlanDetailEntity, Long> {
    List<SolutionPlanDetailEntity> findAllBySolutionPlanEntity(SolutionPlanEntity solutionPlanEntity);
}
