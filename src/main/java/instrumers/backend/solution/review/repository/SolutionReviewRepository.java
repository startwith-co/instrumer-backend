package instrumers.backend.solution.review.repository;

import instrumers.backend.solution.review.model.SolutionReviewEntity;
import org.springframework.stereotype.Component;

@Component
public interface SolutionReviewRepository {
    SolutionReviewEntity save(SolutionReviewEntity solutionReviewEntity);
}
