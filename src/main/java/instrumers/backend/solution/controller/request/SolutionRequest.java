package instrumers.backend.solution.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SolutionRequest {
    public record CreateSolutionRequest(
            @NotBlank(message = "솔루션명은 필수 입력값입니다.")
            String name,

            @NotBlank(message = "솔루션 기본 설명은 필수 입력값입니다.")
            String explanation,

            @NotBlank(message = "솔루션 카테고리는 필수 입력값입니다.")
            String category,

            @NotNull(message = "솔루션 대표 가격은 필수 입력값입니다.")
            Long price,

            @NotBlank(message = "솔루션 웹사이트 링크는 필수 입력값입니다.")
            String webUrl,

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

    public record UpdateSolutionRequest(
            @NotBlank(message = "솔루션명은 필수 입력값입니다.")
            String name,

            @NotBlank(message = "솔루션 기본 설명은 필수 입력값입니다.")
            String explanation,

            @NotBlank(message = "솔루션 카테고리는 필수 입력값입니다.")
            String category,

            @NotNull(message = "솔루션 대표 가격은 필수 입력값입니다.")
            Long price,

            @NotBlank(message = "솔루션 웹사이트 링크는 필수 입력값입니다.")
            String webUrl,

            List<UpdateSolutionImageRequest> images,
            List<UpdateSolutionPlanRequest> plans,
            List<String> keywords
    ) {
        public record UpdateSolutionImageRequest(
                String imageUrl,
                String imageType
        ) {
        }

        public record UpdateSolutionPlanRequest(
                String name,
                String subName,
                Long price,
                String planType,
                List<UpdateSolutionPlanDetailRequest> details
        ) {
        }

        public record UpdateSolutionPlanDetailRequest(
                String name,
                String context
        ) {
        }
    }
}
