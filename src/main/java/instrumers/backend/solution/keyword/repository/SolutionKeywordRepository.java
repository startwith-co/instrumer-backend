package instrumers.backend.solution.keyword.repository;

import instrumers.backend.solution.keyword.model.SolutionKeywordEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionKeywordRepository {
    SolutionKeywordEntity save(SolutionKeywordEntity solutionKeywordEntity);

    List<SolutionKeywordEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
