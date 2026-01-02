package instrumers.backend.user.user.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {
    public record LoginUserRequest(
            @NotBlank(message = "이메일은 필수 입력값입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String email,

            @NotBlank(message = "비밀번호는 필수 입력값입니다.")
            String password
    ) {
    }

    public record ReissueTokenRequest(
            @NotBlank(message = "Refresh Token은 필수 입력값입니다.")
            String refreshToken
    ) {
    }

    public record VerifyEmailAuthKeyRequest(
            @NotBlank(message = "이메일은 필수 입력값입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String email,

            @NotBlank(message = "인증번호는 필수 입력값입니다.")
            String authCode
    ) {
    }
}
