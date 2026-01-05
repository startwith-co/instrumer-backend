package instrumers.backend.common.controller;

import static instrumers.backend.common.controller.response.CommonResponse.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.common.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
@Tag(name = "공통 API")
public class CommonController {
	private final CommonService commonService;

	@PostMapping("/presigned-url")
	@Operation(summary = "S3 Presigned URL 생성 (만료시간: 1시간, 파일명 자동 생성)")
	public ResponseEntity<BaseResponse<PresignedURLResponse>> presignedURL() {
		String presignedUrl = commonService.generatePresignedUrl();
		PresignedURLResponse response = new PresignedURLResponse(presignedUrl);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}
