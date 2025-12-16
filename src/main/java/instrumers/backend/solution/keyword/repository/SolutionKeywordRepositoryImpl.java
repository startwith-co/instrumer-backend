package instrumers.backend.solution.keyword.repository;

import instrumers.backend.solution.keyword.model.SolutionKeywordEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
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
