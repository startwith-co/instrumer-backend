package instrumers.backend.solution.repository.custom;

import instrumers.backend.solution.domain.SolutionPlanDetailEntity;

import java.util.List;

public interface SolutionPlanDetailRepositoryCustom {
    void bulkSave(List<SolutionPlanDetailEntity> planDetails);
}
