package instrumers.backend.solution.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import instrumers.backend.solution.repository.custom.SolutionReviewRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SolutionReviewRepositoryImpl implements SolutionReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;
}

