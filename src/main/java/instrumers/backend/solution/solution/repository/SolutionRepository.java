package instrumers.backend.solution.solution.repository;

import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface SolutionRepository {
    SolutionEntity save(SolutionEntity solutionEntity);

    Optional<SolutionEntity> findBySolutionSeq(Long solutionSeq);

    void delete(SolutionEntity solutionEntity);
}
