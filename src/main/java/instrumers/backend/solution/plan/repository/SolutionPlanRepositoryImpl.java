package instrumers.backend.solution.plan.repository;

import instrumers.backend.solution.plan.model.SolutionPlanEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolutionPlanRepositoryImpl implements SolutionPlanRepository {
    private final SolutionPlanJpaRepository solutionPlanJpaRepository;

    public SolutionPlanEntity save(SolutionPlanEntity solutionPlanEntity) {
        return solutionPlanJpaRepository.save(solutionPlanEntity);
    }

    @Override
    public List<SolutionPlanEntity> findAllBySolutionEntity(SolutionEntity solutionEntity) {
        return solutionPlanJpaRepository.findAllBySolutionEntity(solutionEntity);
    }
}
