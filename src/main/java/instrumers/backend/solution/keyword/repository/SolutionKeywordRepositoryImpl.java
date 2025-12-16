package instrumers.backend.solution.keyword.repository;

import instrumers.backend.solution.keyword.model.SolutionKeywordEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolutionKeywordRepositoryImpl implements SolutionKeywordRepository {
    private final SolutionKeywordJpaRepository solutionKeywordJpaRepository;

    public SolutionKeywordEntity save(SolutionKeywordEntity solutionKeywordEntity) {
        return solutionKeywordJpaRepository.save(solutionKeywordEntity);
    }
}
