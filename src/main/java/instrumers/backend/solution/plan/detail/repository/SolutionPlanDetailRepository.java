package instrumers.backend.solution.plan.detail.repository;

import instrumers.backend.solution.plan.detail.model.SolutionPlanDetailEntity;
import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionPlanDetailRepository {
    SolutionPlanDetailEntity save(SolutionPlanDetailEntity solutionPlanDetailEntity);

    List<SolutionPlanDetailEntity> findAllBySolutionPlanEntity(SolutionPlanEntity solutionPlanEntity);
}
