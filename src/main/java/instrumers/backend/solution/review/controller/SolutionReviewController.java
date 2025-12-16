package instrumers.backend.solution.review.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.solution.review.service.SolutionReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static instrumers.backend.solution.review.controller.request.SolutionReviewRequest.*;
import static instrumers.backend.solution.review.controller.response.SolutionReviewResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/solution-review-service")
@Tag(name = "솔루션 리뷰")
public class SolutionReviewController {
    private final SolutionReviewService solutionReviewService;

    @PostMapping()
    @Operation(summary = "솔루션 정보 저장")
    public ResponseEntity<BaseResponse<CreateSolutionReviewResponse>> create(HttpServletRequest httpServletRequest, @Valid @RequestBody CreateSolutionReviewRequest request) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        CreateSolutionReviewResponse response = solutionReviewService.create(userSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }
}
