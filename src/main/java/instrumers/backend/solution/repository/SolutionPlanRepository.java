package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionPlanEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionPlanRepository {
    SolutionPlanEntity save(SolutionPlanEntity solutionPlanEntity);

    List<SolutionPlanEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
