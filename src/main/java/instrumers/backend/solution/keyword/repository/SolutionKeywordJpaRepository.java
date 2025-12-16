package instrumers.backend.solution.keyword.repository;

import instrumers.backend.solution.keyword.model.SolutionKeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionKeywordJpaRepository extends JpaRepository<SolutionKeywordEntity, Long> {
}
