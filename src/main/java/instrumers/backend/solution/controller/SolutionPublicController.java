package instrumers.backend.solution.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.service.SolutionService;
import instrumers.backend.solution.service.SolutionReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.solution.controller.response.SolutionResponse.*;
import static instrumers.backend.solution.controller.response.SolutionReviewResponse.*;
import static instrumers.backend.solution.controller.response.SolutionVendorResponse.*;

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
		GetSolutionResponse response = solutionService.getSolution(solutionSeq);

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
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		GetSolutionReviewPageResponse response = reviewService.get(solutionSeq, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@GetMapping("/list")
	@Operation(summary = "솔루션 리스트 조회")
	public ResponseEntity<BaseResponse<GetSolutionListResponse>> getSolutionList(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Long minPrice,
			@RequestParam(required = false) Long maxPrice,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		GetSolutionListResponse response = solutionService.getSolutionList(category, minPrice, maxPrice, pageable);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}
