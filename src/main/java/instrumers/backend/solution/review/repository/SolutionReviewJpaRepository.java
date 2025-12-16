package instrumers.backend.solution.review.repository;

import instrumers.backend.solution.review.model.SolutionReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionReviewJpaRepository extends JpaRepository<SolutionReviewEntity, Long> {
}
