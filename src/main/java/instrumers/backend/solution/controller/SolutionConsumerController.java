package instrumers.backend.solution.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.service.SolutionReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static instrumers.backend.solution.controller.request.SolutionReviewRequest.*;
import static instrumers.backend.solution.controller.response.SolutionReviewResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumer/solutions")
@Tag(name = "솔루션 리뷰 API (Consumer)")
public class SolutionConsumerController {
    private final SolutionReviewService reviewService;

    @PostMapping("/{solutionSeq}/reviews")
    @Operation(summary = "솔루션 리뷰 작성")
    public ResponseEntity<BaseResponse<CreateSolutionReviewResponse>> createSolutionReview(
            HttpServletRequest httpServletRequest,
            @PathVariable Long solutionSeq,
            @Valid @RequestBody CreateSolutionReviewRequest request
    ) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        CreateSolutionReviewResponse response = reviewService.create(userSeq, solutionSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }
}
