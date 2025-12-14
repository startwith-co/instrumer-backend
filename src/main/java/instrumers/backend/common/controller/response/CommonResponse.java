package instrumers.backend.common.controller.response;

public class CommonResponse {
	public record PresignedURLResponse(
		String url
	) {
	}
}
