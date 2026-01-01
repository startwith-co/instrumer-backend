package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionEntity;
import instrumers.backend.solution.repository.custom.SolutionRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<SolutionEntity, Long>, SolutionRepositoryCustom {
    @EntityGraph(attributePaths = {"userEntity"})
    Optional<SolutionEntity> findBySolutionSeq(Long solutionSeq);
}
