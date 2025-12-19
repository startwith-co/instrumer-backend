package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionPlanDetailEntity;
import instrumers.backend.solution.domain.SolutionPlanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolutionPlanDetailRepositoryImpl implements SolutionPlanDetailRepository {
    private final SolutionPlanDetailJpaRepository solutionPlanDetailJpaRepository;

    public SolutionPlanDetailEntity save(SolutionPlanDetailEntity solutionPlanEntity) {
        return solutionPlanDetailJpaRepository.save(solutionPlanEntity);
    }

    @Override
    public List<SolutionPlanDetailEntity> findAllBySolutionPlanEntity(SolutionPlanEntity solutionPlanEntity) {
        return solutionPlanDetailJpaRepository.findAllBySolutionPlanEntity(solutionPlanEntity);
    }
}
