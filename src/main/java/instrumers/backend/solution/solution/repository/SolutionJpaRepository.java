package instrumers.backend.solution.solution.repository;

import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionJpaRepository extends JpaRepository<SolutionEntity, Long> {
    Optional<SolutionEntity> findBySolutionSeq(Long solutionSeq);
}
