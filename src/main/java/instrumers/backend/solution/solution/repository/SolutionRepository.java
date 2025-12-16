package instrumers.backend.solution.solution.repository;

import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.stereotype.Component;

@Component
public interface SolutionRepository {
    SolutionEntity save(SolutionEntity solutionEntity);
}
