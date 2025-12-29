package instrumers.backend.solution.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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

	public record GetSolutionReviewInfoResponse(
		Long cnt,
		Double average
	) {
	}

	public record GetSolutionReviewPageResponse(
		List<GetSolutionReviewResponse> content,
		int page,
		int size,
		long totalElements,
		int totalPages,
		boolean hasNext,
		boolean hasPrevious
	) {
	}
}
