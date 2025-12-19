package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionPlanDetailEntity;
import instrumers.backend.solution.domain.SolutionPlanEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionPlanDetailRepository {
    SolutionPlanDetailEntity save(SolutionPlanDetailEntity solutionPlanDetailEntity);

    List<SolutionPlanDetailEntity> findAllBySolutionPlanEntity(SolutionPlanEntity solutionPlanEntity);
}
