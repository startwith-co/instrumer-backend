package instrumers.backend.solution.review.controller.response;

import java.time.LocalDateTime;

public class SolutionReviewResponse {
    public record CreateSolutionReviewResponse(
            Long solutionReviewSeq
    ) {
    }

    public record GetSolutionReviewResponse(
            Long solutionReviewSeq,
            String profileImageUrl,
            String businessName,
            Double rate,
            String context,
            LocalDateTime createdAt
    ) {
    }
}
