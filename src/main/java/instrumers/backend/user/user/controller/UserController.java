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
	@Operation(summary = "사용자 조회")
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
