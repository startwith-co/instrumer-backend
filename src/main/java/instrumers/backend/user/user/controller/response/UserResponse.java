package instrumers.backend.user.user.controller.response;

public class UserResponse {
    public record LoginUserResponse(
            String accessToken,
            String refreshToken
    ) {

    }

    public record ReIssueTokenResponse(
            String accessToken,
            String refreshToken
    ) {

    }
}
