package instrumers.backend.common.controller;

import static instrumers.backend.common.controller.request.CommonRequest.*;
import static instrumers.backend.common.controller.response.CommonResponse.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.HttpMethod;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.common.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-service")
@Tag(name = "공통 API")
public class CommonController {
	private final CommonService commonService;

	@PostMapping("/auth/presigned-url")
	@Operation(summary = "업로드용 S3 Presigned URL 생성")
	public ResponseEntity<BaseResponse<PresignedURLResponse>> presignedURL(@Valid @RequestBody PresignedURLRequest request) {
		String presignedUrl = commonService.generatePresignedUrl(request.fileName(), HttpMethod.PUT, request.expiration());
		PresignedURLResponse response = new PresignedURLResponse(presignedUrl);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}
