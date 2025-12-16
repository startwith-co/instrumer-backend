package instrumers.backend.solution.review.repository;

import instrumers.backend.solution.review.model.SolutionReviewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolutionReviewRepositoryImpl implements SolutionReviewRepository {
    private final SolutionReviewJpaRepository solutionReviewJpaRepository;

    public SolutionReviewEntity save(SolutionReviewEntity solutionReviewEntity) {
        return solutionReviewJpaRepository.save(solutionReviewEntity);
    }
}
