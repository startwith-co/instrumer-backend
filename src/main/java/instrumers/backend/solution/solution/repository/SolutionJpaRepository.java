package instrumers.backend.solution.solution.repository;

import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionJpaRepository extends JpaRepository<SolutionEntity, Long> {
}
