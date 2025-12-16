package instrumers.backend.solution.keyword.repository;

import instrumers.backend.solution.keyword.model.SolutionKeywordEntity;
import org.springframework.stereotype.Component;

@Component
public interface SolutionKeywordRepository {
    SolutionKeywordEntity save(SolutionKeywordEntity solutionKeywordEntity);
}
