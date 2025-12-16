package instrumers.backend.solution.solution.repository;

import instrumers.backend.solution.solution.model.SolutionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SolutionRepositoryImpl implements SolutionRepository {
    private final SolutionJpaRepository solutionJpaRepository;

    public SolutionEntity save(SolutionEntity solutionEntity) {
        return solutionJpaRepository.save(solutionEntity);
    }

    @Override
    public Optional<SolutionEntity> findBySolutionSeq(Long solutionSeq) {
        return solutionJpaRepository.findBySolutionSeq(solutionSeq);
    }
}
