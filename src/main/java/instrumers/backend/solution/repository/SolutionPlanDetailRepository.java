package instrumers.backend.solution.repository;

import instrumers.backend.solution.domain.SolutionPlanDetailEntity;
import instrumers.backend.solution.domain.SolutionPlanEntity;
import instrumers.backend.solution.repository.custom.SolutionPlanDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionPlanDetailRepository extends JpaRepository<SolutionPlanDetailEntity, Long>, SolutionPlanDetailRepositoryCustom {
    List<SolutionPlanDetailEntity> findAllBySolutionPlanEntityIn(List<SolutionPlanEntity> solutionPlanEntities);
}
