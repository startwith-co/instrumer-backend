package instrumers.backend.solution.controller.response;

import instrumers.backend.common.dto.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

public class SolutionReviewResponse {
	public record CreateSolutionReviewResponse(
		Long solutionReviewSeq
	) {
	}

	public record GetSolutionReviewInfoResponse(
		Long cnt,
		Double average
	) {
	}

	public record GetSolutionReviewPageResponse(
		List<GetSolutionReviewResponse> content,
		PageInfo pageInfo
	) {
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
}
