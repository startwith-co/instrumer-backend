package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionImageEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolutionImageRepositoryImpl implements SolutionImageRepository {
    private final SolutionImageJpaRepository solutionImageJpaRepository;

    public SolutionImageEntity save(SolutionImageEntity solutionImageEntity) {
        return solutionImageJpaRepository.save(solutionImageEntity);
    }

    @Override
    public List<SolutionImageEntity> findAllBySolutionEntity(SolutionEntity solutionEntity) {
        return solutionImageJpaRepository.findAllBySolutionEntity(solutionEntity);
    }
}
