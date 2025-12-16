package instrumers.backend.solution.review.repository;

import instrumers.backend.solution.review.model.SolutionReviewEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolutionReviewRepositoryImpl implements SolutionReviewRepository {
    private final SolutionReviewJpaRepository solutionReviewJpaRepository;

    public SolutionReviewEntity save(SolutionReviewEntity solutionReviewEntity) {
        return solutionReviewJpaRepository.save(solutionReviewEntity);
    }

    @Override
    public List<SolutionReviewEntity> findAllBySolutionEntity(SolutionEntity solutionEntity) {
        return solutionReviewJpaRepository.findAllBySolutionEntity(solutionEntity);
    }
}
