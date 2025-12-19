package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionKeywordEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolutionKeywordRepositoryImpl implements SolutionKeywordRepository {
    private final SolutionKeywordJpaRepository solutionKeywordJpaRepository;

    public SolutionKeywordEntity save(SolutionKeywordEntity solutionKeywordEntity) {
        return solutionKeywordJpaRepository.save(solutionKeywordEntity);
    }

    @Override
    public List<SolutionKeywordEntity> findAllBySolutionEntity(SolutionEntity solutionEntity) {
        return solutionKeywordJpaRepository.findAllBySolutionEntity(solutionEntity);
    }
}
