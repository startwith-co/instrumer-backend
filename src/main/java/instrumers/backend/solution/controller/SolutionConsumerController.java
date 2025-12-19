package instrumers.backend.solution.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.review.service.SolutionReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.solution.review.controller.request.SolutionReviewRequest.*;
import static instrumers.backend.solution.review.controller.response.SolutionReviewResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumer/solutions")
@Tag(name = "솔루션 리뷰 API (Consumer)")
public class SolutionConsumerController {

    private final SolutionReviewService reviewService;

    /**
     * 솔루션 리뷰 작성
     * - CONSUMER 권한 필요
     * - 특정 솔루션에 대한 후기 작성
     */
    @PostMapping("/{solutionSeq}/reviews")
    @Operation(summary = "솔루션 리뷰 작성")
    public ResponseEntity<BaseResponse<CreateSolutionReviewResponse>> createReview(
            HttpServletRequest request,
            @PathVariable Long solutionSeq,
            @Valid @RequestBody CreateSolutionReviewRequest dto) {

        Long userSeq = (Long) request.getAttribute("userSeq");

        CreateSolutionReviewRequest requestWithPathSeq = new CreateSolutionReviewRequest(
                solutionSeq, 
                dto.context(),
                dto.rate()
        );

        CreateSolutionReviewResponse response = reviewService.create(userSeq, requestWithPathSeq);

        return ResponseEntity.ok()
                .body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }
}
