package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionReviewEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionReviewRepository {
    SolutionReviewEntity save(SolutionReviewEntity solutionReviewEntity);

    List<SolutionReviewEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
