package instrumers.backend.solution.plan.repository;

import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionPlanRepository {
    SolutionPlanEntity save(SolutionPlanEntity solutionPlanEntity);

    List<SolutionPlanEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
