package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionKeywordEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionKeywordRepository extends JpaRepository<SolutionKeywordEntity, Long> {
    List<SolutionKeywordEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
