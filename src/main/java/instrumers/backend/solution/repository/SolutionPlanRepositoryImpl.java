package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionPlanEntity;
import instrumers.backend.solution.domain.SolutionEntity;
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
