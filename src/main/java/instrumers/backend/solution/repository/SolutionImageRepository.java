package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionImageEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SolutionImageRepository {
    SolutionImageEntity save(SolutionImageEntity solutionImageEntity);

    List<SolutionImageEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
