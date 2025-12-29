package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionReviewEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionReviewRepository extends JpaRepository<SolutionReviewEntity, Long> {
    @Query("""
            SELECT sr
            FROM SolutionReviewEntity sr
            JOIN FETCH sr.solutionEntity s
            JOIN FETCH sr.userEntity u
            WHERE s = :solutionEntity AND sr.deleted = false
            ORDER BY sr.createdAt DESC
            """)
    List<SolutionReviewEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
