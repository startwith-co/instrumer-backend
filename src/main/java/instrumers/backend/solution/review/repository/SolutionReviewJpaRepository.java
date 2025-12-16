package instrumers.backend.solution.review.repository;

import instrumers.backend.solution.review.model.SolutionReviewEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionReviewJpaRepository extends JpaRepository<SolutionReviewEntity, Long> {
    @Query("""
            SELECT sr
            FROM SolutionReviewEntity sr
            JOIN FETCH sr.solutionEntity s
            JOIN FETCH sr.userEntity u
            WHERE s = :solutionEntity
            ORDER BY sr.createdAt DESC
            """)
    List<SolutionReviewEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
