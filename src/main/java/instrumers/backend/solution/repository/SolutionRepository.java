package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<SolutionEntity, Long> {
    Optional<SolutionEntity> findBySolutionSeq(Long solutionSeq);
}
