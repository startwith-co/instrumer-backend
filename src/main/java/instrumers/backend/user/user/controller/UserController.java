package instrumers.backend.user.user.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.common.service.CommonService;
import instrumers.backend.exception.BadRequestException;
import instrumers.backend.user.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static instrumers.backend.user.user.controller.request.UserRequest.*;
import static instrumers.backend.user.user.controller.response.UserResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service")
@Tag(name = "회원")
public class UserController {
    private final UserService userService;
    private final CommonService commonService;

    @PostMapping(value = "/auth/login")
    @Operation(summary = "회원 로그인")
    public ResponseEntity<BaseResponse<LoginUserResponse>> register(@Valid @RequestBody LoginUserRequest request) {
        LoginUserResponse response = userService.Login(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PostMapping(value = "/auth/reissue-token")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<BaseResponse<ReIssueTokenResponse>> reissueToken(@Valid @RequestBody ReissueTokenRequest request) {
        ReIssueTokenResponse response = userService.reissueToken(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PostMapping(value = "/auth/send-email")
    @Operation(summary = "이메일 전송 (유효시간 5분)")
    public ResponseEntity<BaseResponse<Map<String, String>>> sendEmail(@Valid @RequestBody SendEmailRequest request) {
        String authCode = commonService.sendAuthCode(request.email());
        Map<String, String> response = new HashMap<>();
        response.put("authCode", authCode);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PostMapping(value = "/auth/auth-code")
    @Operation(summary = "이메일 인증 번호 확인")
    public ResponseEntity<BaseResponse<String>> verifyAuthKey(@Valid @RequestBody VerifyEmailAuthKeyRequest request) {
        if (!commonService.verifyAuthCode(request.email(), request.authCode())) {
            throw new BadRequestException(
                    HttpStatus.BAD_REQUEST.value(),
                    "인증번호가 틀렸습니다."
            );
        }

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}
