package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionKeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionKeywordRepository extends JpaRepository<SolutionKeywordEntity, Long> {
}
