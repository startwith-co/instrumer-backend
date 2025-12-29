package instrumers.backend.solution.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.service.SolutionService;
import instrumers.backend.solution.service.SolutionReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.solution.controller.dto.response.SolutionResponse.*;
import static instrumers.backend.solution.controller.dto.response.SolutionReviewResponse.*;
import static instrumers.backend.solution.controller.dto.response.SolutionVendorResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/solutions")
@Tag(name = "솔루션 API (Public)")
public class SolutionPublicController {
	private final SolutionService solutionService;
	private final SolutionReviewService reviewService;

	/**
	 * 솔루션 개별 조회
	 * - 인증 불필요
	 * - 누구나 솔루션 상세 정보 확인 가능
	 */
	@GetMapping("/{solutionSeq}")
	@Operation(summary = "솔루션 상세 조회")
	public ResponseEntity<BaseResponse<GetSolutionResponse>> getSolution(@PathVariable Long solutionSeq) {
		GetSolutionResponse response = solutionService.get(solutionSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	/**
	 * 솔루션 리뷰 목록 조회 (페이징)
	 * - 특정 솔루션의 리뷰를 페이징하여 조회
	 * - 인증 불필요
	 * - 기본값: page=0, size=10, sort=createdAt,DESC
	 */
	@GetMapping("/{solutionSeq}/reviews")
	@Operation(summary = "솔루션 리뷰 목록 조회 (페이징)")
	public ResponseEntity<BaseResponse<GetSolutionReviewPageResponse>> getReviews(
			@PathVariable Long solutionSeq,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		GetSolutionReviewPageResponse response = reviewService.get(solutionSeq, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	/**
	 * 솔루션 리뷰 통계 정보 조회
	 * - 평점 평균 및 리뷰 개수
	 * - 인증 불필요
	 */
	@GetMapping("/{solutionSeq}/review")
	@Operation(summary = "솔루션 리뷰 정보 조회 (평점, 개수)")
	public ResponseEntity<BaseResponse<GetSolutionReviewInfoResponse>> getSolutionReviewInfo(@PathVariable Long solutionSeq) {
		GetSolutionReviewInfoResponse response = reviewService.getSolutionReviewInfo(solutionSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping("/{solutionSeq}/vendor")
	@Operation(summary = "솔루션 생성 벤더 정보 조회")
	public ResponseEntity<BaseResponse<GetSolutionVendorResponse>> getSolutionVendor(@PathVariable Long solutionSeq) {
		GetSolutionVendorResponse response = solutionService.getSolutionVendor(solutionSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}
