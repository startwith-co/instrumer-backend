package instrumers.backend.solution.controller.response;

public class SolutionVendorResponse {
	public record GetSolutionVendorResponse(
		Long vendorSeq,
		String businessName
	) {
	}
}
