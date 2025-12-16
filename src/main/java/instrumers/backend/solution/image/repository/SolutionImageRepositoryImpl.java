package instrumers.backend.solution.image.repository;

import instrumers.backend.solution.image.model.SolutionImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolutionImageRepositoryImpl implements SolutionImageRepository {
    private final SolutionImageJpaRepository solutionImageJpaRepository;

    public SolutionImageEntity save(SolutionImageEntity solutionImageEntity) {
        return solutionImageJpaRepository.save(solutionImageEntity);
    }
}
