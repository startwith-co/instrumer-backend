package instrumers.backend.user.user.service;

import instrumers.backend.common.service.CommonService;
import instrumers.backend.exception.BadRequestException;
import instrumers.backend.exception.NotFoundException;
import instrumers.backend.exception.UnauthorizedException;
import instrumers.backend.user.user.model.UserEntity;
import instrumers.backend.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static instrumers.backend.user.user.controller.request.UserRequest.*;
import static instrumers.backend.user.user.controller.response.UserResponse.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CommonService commonService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginUserResponse Login(LoginUserRequest request) {
        UserEntity userEntity = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.CONFLICT.value(),
                        "존재하지 않는 이메일입니다."
                ));

        if (!bCryptPasswordEncoder.matches(request.password(), userEntity.getPassword())) {
            throw new BadRequestException(
                    HttpStatus.BAD_REQUEST.value(),
                    "비밀번호가 일치하지 않습니다."
            );
        }

        String accessToken = commonService.issueToken(userEntity.getUserSeq(), "ACCESS");
        String refreshToken = commonService.issueToken(userEntity.getUserSeq(), "REFRESH");
        commonService.saveToken(userEntity.getUserSeq(), "WHITE", accessToken);

        return new LoginUserResponse(accessToken, refreshToken);
    }

    public ReIssueTokenResponse reissueToken(ReissueTokenRequest request) {
        var claims = commonService.parseToken(request.refreshToken());
        String type = claims.get("type", String.class);
        Long userSeq = ((Number) claims.get("userSeq")).longValue();

        if (!"REFRESH".equalsIgnoreCase(type)) {
            throw new UnauthorizedException(
                    HttpStatus.UNAUTHORIZED.value(),
                    "REFRESH 토큰만 사용할 수 있습니다."
            );
        }

        userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        "존재하지 않는 회원입니다."
                ));

        if (commonService.isTokenInWhiteList(userSeq, request.refreshToken())) {
            throw new UnauthorizedException(
                    HttpStatus.UNAUTHORIZED.value(),
                    "유효하지 않은 Refresh Token입니다."
            );
        }

        if (commonService.isTokenInBlackList(userSeq, request.refreshToken())) {
            throw new UnauthorizedException(
                    HttpStatus.UNAUTHORIZED.value(),
                    "이미 사용된 Refresh Token입니다."
            );
        }

        commonService.deleteToken(userSeq, "WHITE");
        commonService.saveToken(userSeq, "BLACK", request.refreshToken());

        String accessToken = commonService.issueToken(userSeq, "ACCESS");
        String refreshToken = commonService.issueToken(userSeq, "REFRESH");

        commonService.saveToken(userSeq, "WHITE", accessToken);

        return new ReIssueTokenResponse(accessToken, refreshToken);
    }
}
