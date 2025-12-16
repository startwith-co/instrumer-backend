package instrumers.backend.solution.review.controller.request;

public class SolutionReviewRequest {
    public record CreateSolutionReviewRequest(
            Long solutionSeq,
            String context,
            Double rate
    ) {
    }
}
