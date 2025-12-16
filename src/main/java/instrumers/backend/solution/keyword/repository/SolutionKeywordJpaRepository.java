package instrumers.backend.solution.keyword.repository;

import instrumers.backend.solution.keyword.model.SolutionKeywordEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionKeywordJpaRepository extends JpaRepository<SolutionKeywordEntity, Long> {
    List<SolutionKeywordEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
