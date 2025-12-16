package instrumers.backend.solution.plan.repository;

import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import org.springframework.stereotype.Component;

@Component
public interface SolutionPlanRepository {
    SolutionPlanEntity save(SolutionPlanEntity solutionPlanEntity);
}
