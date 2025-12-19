package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionKeywordEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionKeywordRepository {
    SolutionKeywordEntity save(SolutionKeywordEntity solutionKeywordEntity);

    List<SolutionKeywordEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
