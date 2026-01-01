package instrumers.backend.solution.repository.custom;

import instrumers.backend.solution.domain.SolutionImageEntity;

import java.util.List;

public interface SolutionImageRepositoryCustom {
    void bulkSave(List<SolutionImageEntity> images);
}
