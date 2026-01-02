package instrumers.backend.user.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.user.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "회원 권한 필요")
public class UserController {
	private final UserService userService;

	@GetMapping()
	@Operation(
		summary = "사용자 조회",
		description = """
			로그인한 사용자의 정보를 조회합니다.
			
			userType에 따라 응답 구조가 달라집니다.
			
			VENDOR 응답 예시
			```json
			{
			  "userSeq": 1,
			  "email": "vendor@example.com",
			  "userType": "VENDOR",
			  "profileImageUrl": "https://cdn.example.com/profile.png",
			  "businessName": "인스트루머스",
			  "managerName": "김대표",
			  "phone": "010-1234-5678",
			  "bank": "KB국민은행",
			  "account": "123456-01-123456"
			}
			```
			
			CONSUMER 응답 예시
			```json
			{
			  "userSeq": 2,
			  "email": "consumer@example.com",
			  "userType": "CONSUMER",
			  "profileImageUrl": "https://cdn.example.com/profile.png",
			  "businessName": "테스트컴퍼니",
			  "managerName": "이담당",
			  "phone": "010-9876-5432"
			}
			```
			"""
	)
	public ResponseEntity<BaseResponse<Object>> get(HttpServletRequest httpServletRequest) {
		Long userSeq = (Long)httpServletRequest.getAttribute("userSeq");
		Object response = userService.get(userSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	@DeleteMapping()
	@Operation(summary = "사용자 삭제")
	public ResponseEntity<BaseResponse<String>> delete(HttpServletRequest httpServletRequest) {
		Long userSeq = (Long)httpServletRequest.getAttribute("userSeq");
		userService.delete(userSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}
