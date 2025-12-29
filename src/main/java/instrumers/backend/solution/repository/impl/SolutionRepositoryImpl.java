package instrumers.backend.solution.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import instrumers.backend.solution.domain.QSolutionEntity;
import instrumers.backend.solution.domain.SolutionEntity;
import instrumers.backend.solution.repository.custom.SolutionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SolutionRepositoryImpl implements SolutionRepositoryCustom {
	private final JPAQueryFactory queryFactory;
}

