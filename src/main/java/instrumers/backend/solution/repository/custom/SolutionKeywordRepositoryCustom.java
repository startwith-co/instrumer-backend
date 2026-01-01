package instrumers.backend.solution.repository.custom;

import instrumers.backend.solution.domain.SolutionKeywordEntity;

import java.util.List;

public interface SolutionKeywordRepositoryCustom {
    void bulkSave(List<SolutionKeywordEntity> keywords);
}

