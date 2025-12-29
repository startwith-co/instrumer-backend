package instrumers.backend.solution.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolutionReviewRequest {
    public record CreateSolutionReviewRequest(
            @NotNull(message = "솔루션 SEQ 값은 필수 입력값입니다.")
            Long solutionSeq,

            @NotBlank(message = "리뷰 내용은 필수 입력값입니다.")
            String context,

            @NotNull(message = "리뷰 점수는 필수 입력값입니다.")
            Double rate
    ) {
    }
}
