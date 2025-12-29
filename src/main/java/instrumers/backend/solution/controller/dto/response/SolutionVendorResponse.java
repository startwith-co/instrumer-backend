package instrumers.backend.solution.controller.dto.response;

public class SolutionVendorResponse {
	public record GetSolutionVendorResponse(
		Long vendorSeq,
		String businessName
	) {
	}
}
