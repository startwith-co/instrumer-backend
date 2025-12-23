package instrumers.backend.solution.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.service.SolutionService;
import instrumers.backend.solution.service.SolutionReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static instrumers.backend.solution.controller.dto.response.SolutionResponse.*;
import static instrumers.backend.solution.controller.dto.response.SolutionReviewResponse.*;

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
        GetSolutionResponse response = solutionService.get(null, solutionSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    /**
     * 솔루션 리뷰 목록 조회
     * - 특정 솔루션의 모든 리뷰 확인
     * - 인증 불필요
     */
    @GetMapping("/{solutionSeq}/reviews")
    @Operation(summary = "솔루션 리뷰 목록 조회")
    public ResponseEntity<BaseResponse<List<GetSolutionReviewResponse>>> getReviews(@PathVariable Long solutionSeq) {
        List<GetSolutionReviewResponse> response = reviewService.get(null, solutionSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }
}
