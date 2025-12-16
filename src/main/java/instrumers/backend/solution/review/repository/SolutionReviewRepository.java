package instrumers.backend.solution.review.repository;

import instrumers.backend.solution.review.model.SolutionReviewEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionReviewRepository {
    SolutionReviewEntity save(SolutionReviewEntity solutionReviewEntity);

    List<SolutionReviewEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
