package instrumers.backend.common.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommonRequest {
	public record PresignedURLRequest(
		@NotBlank(message = "S3에 저장될 파일명은 필수 입력값입니다.")
		String fileName,

		@NotNull(message = "만료시간(분)은 필수 입력값입니다.")
		int expiration
	) {
	}
}
