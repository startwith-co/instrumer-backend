package instrumers.backend.solution.image.repository;

import instrumers.backend.solution.image.model.SolutionImageEntity;
import org.springframework.stereotype.Component;

@Component
public interface SolutionImageRepository {
    SolutionImageEntity save(SolutionImageEntity solutionImageEntity);
}
