package instrumers.backend.user.user.controller;

import instrumers.backend.base.BaseResponse;
import instrumers.backend.common.service.CommonService;
import instrumers.backend.exception.BadRequestException;
import instrumers.backend.user.consumer.service.ConsumerService;
import instrumers.backend.user.user.controller.response.UserResponse;
import instrumers.backend.user.user.service.UserService;
import instrumers.backend.user.vendor.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static instrumers.backend.user.user.controller.request.UserRequest.*;
import static instrumers.backend.user.user.controller.response.UserResponse.*;
import static instrumers.backend.user.consumer.controller.request.ConsumerRequest.*;
import static instrumers.backend.user.vendor.controller.request.VendorRequest.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
@Tag(name = "회원 권한 불필요")
public class UserPublicController {
    private final UserService userService;
    private final CommonService commonService;
    private final ConsumerService consumerService;
    private final VendorService vendorService;

    @PostMapping(value = "/login")
    @Operation(summary = "회원 로그인")
    public ResponseEntity<BaseResponse<LoginUserResponse>> register(@Valid @RequestBody LoginUserRequest request) {
        LoginUserResponse response = userService.Login(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PostMapping(value = "/reissue-token")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<BaseResponse<ReIssueTokenResponse>> reissueToken(@Valid @RequestBody ReissueTokenRequest request) {
        ReIssueTokenResponse response = userService.reissueToken(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @GetMapping("/send-email/{email}")
    public Mono<ResponseEntity<BaseResponse<String>>> sendEmail(@PathVariable String email) {
        return commonService.sendAuthCode(email)
                .thenReturn(ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS")));
    }

    @PostMapping(value = "/auth-code")
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

    @PostMapping(value = "/register/consumer")
    @Operation(summary = "수요 고객 회원가입")
    public ResponseEntity<BaseResponse<String>> registerConsumer(@Valid @RequestBody RegisterConsumerRequest request) {
        consumerService.save(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @PostMapping(value = "/register/vendor")
    @Operation(summary = "벤더 기업 회원가입")
    public ResponseEntity<BaseResponse<String>> registerVendor(@Valid @RequestBody RegisterVendorRequest request) {
        vendorService.save(request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @GetMapping("/{userSeq}")
    @Operation(
            summary = "특정 사용자 정보 조회",
            description = """
                    특정 사용자의 정보를 조회합니다.
                    
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
    public ResponseEntity<BaseResponse<Object>> get(@PathVariable Long userSeq) {
        Object response = userService.get(userSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

}
