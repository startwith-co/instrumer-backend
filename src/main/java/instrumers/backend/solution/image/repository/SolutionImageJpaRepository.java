package instrumers.backend.solution.image.repository;

import instrumers.backend.solution.image.model.SolutionImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionImageJpaRepository extends JpaRepository<SolutionImageEntity, Integer> {
}
