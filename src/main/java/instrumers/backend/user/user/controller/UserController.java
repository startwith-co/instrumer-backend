package instrumers.backend.user.user.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "회원 권한 필요")
public class UserController {
    private final UserService userService;

    @GetMapping()
    @Operation(summary = "본인 정보 조회")
    @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "VENDOR",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "SUCCESS",
                                              "data": {
                                                "userSeq": 1,
                                                "email": "vendor@example.com",
                                                "userType": "VENDOR",
                                                "profileImageUrl": "https://cdn.example.com/profile.png",
                                                "businessName": "인스트루머스",
                                                "managerName": "홍길동",
                                                "phone": "010-1234-5678",
                                                "bank": "KB국민은행",
                                                "account": "123456-01-123456"
                                              }
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "CONSUMER",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "SUCCESS",
                                              "data": {
                                                "userSeq": 2,
                                                "email": "consumer@example.com",
                                                "userType": "CONSUMER",
                                                "profileImageUrl": "https://cdn.example.com/profile.png",
                                                "businessName": "테스트컴퍼니",
                                                "managerName": "김철수",
                                                "phone": "010-9876-5432"
                                              }
                                            }
                                            """
                            )
                    }
            )
    )
    public ResponseEntity<BaseResponse<Object>> get(HttpServletRequest httpServletRequest) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        Object response = userService.get(userSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
    }

    @PutMapping()
    @Operation(summary = "본인 정보 수정")
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "VENDOR",
                                    value = """
                                            {
                                              "email": "vendor@example.com",
                                              "password": "newPassword123",
                                              "profileImageUrl": "https://cdn.example.com/profile.png",
                                              "businessName": "인스트루머스",
                                              "phone": "010-1234-5678",
                                              "bank": "KB국민은행",
                                              "account": "123456-01-123456"
                                            }
                                            """
                            ),
                            @ExampleObject(
                                    name = "CONSUMER",
                                    value = """
                                            {
                                              "email": "consumer@example.com",
                                              "password": "newPassword123",
                                              "profileImageUrl": "https://cdn.example.com/profile.png",
                                              "businessName": "테스트컴퍼니",
                                              "phone": "010-9876-5432"
                                            }
                                            """
                            )
                    }
            )
    )
    public ResponseEntity<BaseResponse<String>> update(HttpServletRequest httpServletRequest, @Valid @RequestBody Object request) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        userService.update(userSeq, request);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }

    @DeleteMapping()
    @Operation(summary = "본인 계정 삭제")
    public ResponseEntity<BaseResponse<String>> delete(HttpServletRequest httpServletRequest) {
        Long userSeq = (Long) httpServletRequest.getAttribute("userSeq");
        userService.delete(userSeq);

        return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
    }
}
