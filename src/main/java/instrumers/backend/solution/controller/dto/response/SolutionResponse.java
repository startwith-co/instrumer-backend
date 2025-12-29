package instrumers.backend.solution.controller.dto.response;

import java.util.List;

public class SolutionResponse {
	public record CreateSolutionResponse(
		Long solutionSeq
	) {
	}

	public record GetSolutionResponse(
		Long solutionSeq,
		String name,
		String explanation,
		String category,
		Long price,
		List<GetSolutionImageRequest> images,
		List<GetSolutionPlanRequest> plans,
		List<String> keywords
	) {
		public record GetSolutionImageRequest(
			String imageUrl,
			String imageType
		) {
		}

		public record GetSolutionPlanRequest(
			String name,
			String subName,
			Long price,
			String planType,
			List<GetSolutionPlanDetailRequest> details
		) {
		}

		public record GetSolutionPlanDetailRequest(
			String name,
			String context
		) {
		}
	}
}
