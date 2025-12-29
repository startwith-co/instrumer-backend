package instrumers.backend.solution.repository.custom;

import instrumers.backend.solution.controller.response.SolutionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static instrumers.backend.solution.controller.response.SolutionResponse.*;
import static instrumers.backend.solution.controller.response.SolutionResponse.GetSolutionListResponse.*;

public interface SolutionRepositoryCustom {
	/**
	 * 솔루션 리스트 조회 (QueryDSL)
	 * - 솔루션 정보, 대표 이미지, 리뷰 통계, 벤더 정보 포함
	 *
	 * @param category 카테고리 (null 가능, 선택 조건)
	 * @param minPrice 최소 가격 (null 가능, price >= minPrice)
	 * @param maxPrice 최대 가격 (null 가능, price < maxPrice)
	 * @param pageable 페이징 정보
	 * @return 솔루션 리스트 응답 페이지
	 */
	Page<GetSolutionList> findSolutionList(String category, Long minPrice, Long maxPrice, Pageable pageable);
}

