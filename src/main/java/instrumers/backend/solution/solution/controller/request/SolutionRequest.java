package instrumers.backend.solution.solution.controller.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class SolutionRequest {
    public record CreateSolutionRequest(
            @NotBlank(message = "솔루션명은 필수 입력값입니다.")
            String name,

            @NotBlank(message = "솔루션 기본 설명은 필수 입력값입니다.")
            String explanation,

            @NotBlank(message = "솔루션 카테고리는 필수 입력값입니다.")
            String category,

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
