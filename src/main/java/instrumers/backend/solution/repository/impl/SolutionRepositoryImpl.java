package instrumers.backend.solution.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import instrumers.backend.solution.domain.QSolutionEntity;
import instrumers.backend.solution.domain.QSolutionImageEntity;
import instrumers.backend.solution.domain.QSolutionReviewEntity;
import instrumers.backend.solution.repository.custom.SolutionRepositoryCustom;
import instrumers.backend.user.user.model.QUserEntity;
import instrumers.backend.user.vendor.model.QVendorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static instrumers.backend.solution.controller.response.SolutionResponse.GetSolutionListResponse.*;

@Repository
@RequiredArgsConstructor
public class SolutionRepositoryImpl implements SolutionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetSolutionList> findSolutionList(String category, Long minPrice, Long maxPrice, Pageable pageable) {
        QSolutionEntity solution = QSolutionEntity.solutionEntity;
        QSolutionImageEntity solutionImage = QSolutionImageEntity.solutionImageEntity;
        QSolutionReviewEntity solutionReview = QSolutionReviewEntity.solutionReviewEntity;
        QUserEntity user = QUserEntity.userEntity;
        QVendorEntity vendor = QVendorEntity.vendorEntity;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(solution.deleted.eq(false));

        if (category != null && !category.isBlank()) builder.and(solution.category.eq(category));
        if (minPrice != null) builder.and(solution.price.goe(minPrice));
        if (maxPrice != null) builder.and(solution.price.lt(maxPrice));

        // 대표 이미지 서브쿼리 (imageType = 'representation')
        JPQLQuery<String> representationImage = queryFactory
                .select(solutionImage.imageUrl)
                .from(solutionImage)
                .where(
                        solutionImage.solutionEntity.eq(solution)
                                .and(solutionImage.imageType.eq("representation"))
                )
                .limit(1);

        // 리뷰 개수 서브쿼리
        JPQLQuery<Long> reviewCount = queryFactory
                .select(solutionReview.count())
                .from(solutionReview)
                .where(
                        solutionReview.solutionEntity.eq(solution)
                                .and(solutionReview.deleted.eq(false))
                );

        // 리뷰 평균 평점 서브쿼리 (소수점 첫째 자리까지 반올림)
        JPQLQuery<Double> reviewAverage = queryFactory
                .select(Expressions.numberTemplate(
                        Double.class,
                        "ROUND({0}, 1)",
                        solutionReview.rate.avg()
                ))
                .from(solutionReview)
                .where(
                        solutionReview.solutionEntity.eq(solution)
                                .and(solutionReview.deleted.eq(false))
                );

        // 벤더 businessName 서브쿼리
        JPQLQuery<String> businessName = queryFactory
                .select(vendor.businessName)
                .from(vendor)
                .innerJoin(user).on(vendor.userEntity.eq(user))
                .where(user.eq(solution.userEntity))
                .limit(1);

        JPAQuery<GetSolutionList> query = queryFactory
                .select(Projections.constructor(
                        GetSolutionList.class,
                        solution.solutionSeq,
                        representationImage,
                        solution.name,
                        solution.price,
                        reviewCount,
                        reviewAverage,
                        businessName
                ))
                .from(solution)
                .where(builder)
                .orderBy(solution.createdAt.desc());

        List<GetSolutionList> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(solution.count())
                .from(solution)
                .where(builder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}

