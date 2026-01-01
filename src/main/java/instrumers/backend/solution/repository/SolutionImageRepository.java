package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionImageEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import instrumers.backend.solution.repository.custom.SolutionImageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionImageRepository extends JpaRepository<SolutionImageEntity, Integer>, SolutionImageRepositoryCustom {
    List<SolutionImageEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
