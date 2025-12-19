package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionEntity;
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

    @Override
    public void delete(SolutionEntity solutionEntity) {
        solutionJpaRepository.delete(solutionEntity);
    }
}
