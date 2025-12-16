package instrumers.backend.solution.image.repository;

import instrumers.backend.solution.image.model.SolutionImageEntity;
import instrumers.backend.solution.solution.model.SolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionImageJpaRepository extends JpaRepository<SolutionImageEntity, Integer> {
    List<SolutionImageEntity> findAllBySolutionEntity(SolutionEntity solutionEntity);
}
