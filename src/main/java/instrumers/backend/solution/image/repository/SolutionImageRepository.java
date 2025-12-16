package instrumers.backend.solution.image.repository;

import instrumers.backend.solution.image.model.SolutionImageEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionImageRepository {
    SolutionImageEntity save(SolutionImageEntity solutionImageEntity);

    List<SolutionImageEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
