package instrumers.backend.user.user.controller;

import instrumers.backend.user.consumer.controller.response.ConsumerResponse;
import instrumers.backend.user.vendor.controller.response.VendorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.user.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static instrumers.backend.user.consumer.controller.response.ConsumerResponse.*;
import static instrumers.backend.user.vendor.controller.response.VendorResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "회원 권한 필요")
public class UserController {
	private final UserService userService;

	@GetMapping()
	@Operation(summary = "본인 정보 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "VENDOR", description = "VENDOR",
					content = @Content(schema = @Schema(implementation = GetVendorResponse.class))),
			@ApiResponse(responseCode = "CONSUMER", description = "CONSUMER",
					content = @Content(schema = @Schema(implementation = GetConsumerResponse.class)))
	})
	public ResponseEntity<BaseResponse<Object>> get(HttpServletRequest httpServletRequest) {
		Long userSeq = (Long)httpServletRequest.getAttribute("userSeq");
		Object response = userService.get(userSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@PutMapping()
	@Operation(
		summary = "본인 정보 수정",
		description = """
			로그인한 사용자의 정보를 수정합니다.
			
			userType에 따라 요청 구조가 달라집니다.
			
			모든 필드는 선택적이며, 전달된 필드만 업데이트됩니다.
			
			VENDOR 요청 예시
			```json
			{
			  "email": "vendor@example.com",
			  "password": "newPassword123",
			  "profileImageUrl": "https://cdn.example.com/profile.png",
			  "businessName": "인스트루머스",
			  "phone": "010-1234-5678",
			  "bank": "KB국민은행",
			  "account": "123456-01-123456"
			}
			```
			
			CONSUMER 요청 예시
			```json
			{
			  "email": "consumer@example.com",
			  "password": "newPassword123",
			  "profileImageUrl": "https://cdn.example.com/profile.png",
			  "businessName": "테스트컴퍼니",
			  "phone": "010-9876-5432"
			}
			```
			"""
	)
	public ResponseEntity<BaseResponse<String>> update(HttpServletRequest httpServletRequest, @Valid @RequestBody Object request) {
		Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
		userService.update(userSeq, request);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}

	@DeleteMapping()
	@Operation(summary = "본인 계정 삭제")
	public ResponseEntity<BaseResponse<String>> delete(HttpServletRequest httpServletRequest) {
		Long userSeq = (Long)httpServletRequest.getAttribute("userSeq");
		userService.delete(userSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}
