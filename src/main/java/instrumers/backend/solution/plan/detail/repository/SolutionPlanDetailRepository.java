package instrumers.backend.solution.plan.detail.repository;

import instrumers.backend.solution.plan.detail.model.SolutionPlanDetailEntity;
import org.springframework.stereotype.Component;

@Component
public interface SolutionPlanDetailRepository {
    SolutionPlanDetailEntity save(SolutionPlanDetailEntity solutionPlanDetailEntity);
}
