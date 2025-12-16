package instrumers.backend.solution.plan.detail.repository;

import instrumers.backend.solution.plan.detail.model.SolutionPlanDetailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolutionPlanDetailRepositoryImpl implements SolutionPlanDetailRepository {
    private final SolutionPlanDetailJpaRepository solutionPlanDetailJpaRepository;

    public SolutionPlanDetailEntity save(SolutionPlanDetailEntity solutionPlanEntity) {
        return solutionPlanDetailJpaRepository.save(solutionPlanEntity);
    }
}
