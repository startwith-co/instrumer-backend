package instrumers.backend.solution.plan.repository;

import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolutionPlanRepositoryImpl implements SolutionPlanRepository {
    private final SolutionPlanJpaRepository solutionPlanJpaRepository;

    public SolutionPlanEntity save(SolutionPlanEntity solutionPlanEntity) {
        return solutionPlanJpaRepository.save(solutionPlanEntity);
    }
}
