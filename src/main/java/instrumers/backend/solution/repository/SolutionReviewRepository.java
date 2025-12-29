package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionReviewEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionReviewRepository extends JpaRepository<SolutionReviewEntity, Long> {
    @EntityGraph(attributePaths = {"userEntity"})
    @Query("""
            SELECT sr
            FROM SolutionReviewEntity sr
            WHERE sr.solutionEntity = :solutionEntity AND sr.deleted = false
            ORDER BY sr.createdAt DESC
            """)
    Page<SolutionReviewEntity> findAllBySolutionEntity(SolutionEntity solutionEntity, Pageable pageable);

    @Query("""
            SELECT COUNT(sr)
            FROM SolutionReviewEntity sr
            WHERE sr.solutionEntity = :solutionEntity AND sr.deleted = false
            """)
    long countBySolutionEntity(SolutionEntity solutionEntity);

    @Query("""
            SELECT COALESCE(AVG(sr.rate), 0.0)
            FROM SolutionReviewEntity sr
            WHERE sr.solutionEntity = :solutionEntity AND sr.deleted = false
            """)
    Double getAverageRateBySolutionEntity(SolutionEntity solutionEntity);
}
