package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface SolutionRepository {
    SolutionEntity save(SolutionEntity solutionEntity);

    Optional<SolutionEntity> findBySolutionSeq(Long solutionSeq);

    void delete(SolutionEntity solutionEntity);
}
