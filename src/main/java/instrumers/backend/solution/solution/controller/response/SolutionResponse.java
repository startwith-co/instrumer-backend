package instrumers.backend.solution.solution.controller.response;

import java.util.List;

public class SolutionResponse {
    public record CreateSolutionResponse(
            Long solutionSeq
    ) {
    }

    public record GetSolutionResponse(
            String name,
            String explanation,
            String category,
            Long price,
            List<CreateSolutionImageRequest> images,
            List<CreateSolutionPlanRequest> plans,
            List<String> keywords
    ) {
        public record CreateSolutionImageRequest(
                String imageUrl,
                String imageType
        ) {
        }

        public record CreateSolutionPlanRequest(
                String name,
                String subName,
                Long price,
                String planType,
                List<CreateSolutionPlanDetailRequest> details
        ) {
        }

        public record CreateSolutionPlanDetailRequest(
                String name,
                String context
        ) {
        }
    }
}
