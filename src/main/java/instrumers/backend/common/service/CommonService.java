package instrumers.backend.common.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import instrumers.backend.exception.BadRequestException;
import instrumers.backend.exception.ServerException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static instrumers.backend.common.util.CommonUtil.BLACK_KEY_FMT;
import static instrumers.backend.common.util.CommonUtil.EMAIL_AUTH_KEY_FMT;
import static instrumers.backend.common.util.CommonUtil.WHITE_KEY_FMT;

@Service
@RequiredArgsConstructor
public class CommonService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final RedisTemplate<String, String> redisTemplate;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final AmazonS3Client amazonS3Client;

    /**
     * 지정된 만료 시간과 사용자 정보를 기반으로 JWT 토큰을 발급합니다.
     *
     * @param userSeq 사용자 고유 식별자(PK)
     * @param type    토큰 유형 (예: "ACCESS", "REFRESH")
     * @return 서명된 JWT 토큰 문자열
     */
    public String issueToken(Long userSeq, String type) {
        long now = System.currentTimeMillis();
        Date exp = null;
        if (type.equalsIgnoreCase("ACCESS")) exp = new Date(now + accessTokenExpiration);
        if (type.equalsIgnoreCase("REFRESH")) exp = new Date(now + refreshTokenExpiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userSeq", userSeq);
        claims.put("type", type.toUpperCase());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userSeq))
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(now))
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    /**
     * JWT 토큰을 Redis에 저장합니다.
     *
     * @param userSeq Redis 키로 사용할 사용자 식별자
     * @param type    저장 유형 ("WHITE" 또는 "BLACK") — 현재 구현은 키 프리픽스 구분에 사용됩니다
     * @param token   저장할 JWT 토큰 문자열
     */
    public void saveToken(Long userSeq, String type, String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            Date exp = body.getExpiration();
            long now = System.currentTimeMillis();
            long TTL = exp.getTime() - now;

            if (TTL <= 0) {
                throw new BadRequestException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Token expiration time is invalid or already expired"
                );
            }

            String key = "";

            if (type.equalsIgnoreCase("WHITE")) key = String.format(WHITE_KEY_FMT, userSeq);
            if (type.equalsIgnoreCase("BLACK")) key = String.format(BLACK_KEY_FMT, userSeq);

            if (key.isEmpty()) {
                throw new BadRequestException(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid token type: " + type
                );
            }

            redisTemplate.opsForValue().set(
                    key,
                    token,
                    TTL,
                    TimeUnit.MILLISECONDS
            );

            // 저장 확인
            String savedValue = redisTemplate.opsForValue().get(key);
            if (savedValue == null || !savedValue.equals(token)) {
                throw new ServerException(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Failed to save token to Redis"
                );
            }
        } catch (ServerException e) {
            throw e;
        } catch (Exception e) {
            throw new ServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage()
            );
        }
    }

    /**
     * Redis에서 토큰을 삭제합니다.
     *
     * @param userSeq 사용자 고유 식별자(PK)
     * @param type    삭제할 리스트 타입 ("WHITE" 또는 "BLACK")
     *                - "WHITE": auth:whitelist:{userSeq} 키 삭제
     *                - "BLACK": auth:blacklist:{userSeq} 키 삭제
     */
    public void deleteToken(Long userSeq, String type) {
        String key = "";
        if (type.equalsIgnoreCase("WHITE")) key = String.format(WHITE_KEY_FMT, userSeq);
        if (type.equalsIgnoreCase("BLACK")) key = String.format(BLACK_KEY_FMT, userSeq);

        redisTemplate.delete(key);
    }

    /**
     * JWT 토큰이 WHITE 리스트에 있는지 확인합니다.
     *
     * @param userSeq 사용자 고유 식별자(PK)
     * @param token   확인할 JWT 토큰 문자열
     * @return WHITE 리스트에 토큰이 존재하면 true, 아니면 false
     */
    public boolean isTokenInWhiteList(Long userSeq, String token) {
        String key = String.format(WHITE_KEY_FMT, userSeq);
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(token);
    }

    /**
     * JWT 토큰이 BLACK 리스트에 있는지 확인합니다.
     *
     * @param userSeq 사용자 고유 식별자(PK)
     * @param token   확인할 JWT 토큰 문자열
     * @return BLACK 리스트에 토큰이 존재하면 true, 아니면 false
     */
    public boolean isTokenInBlackList(Long userSeq, String token) {
        String key = String.format(BLACK_KEY_FMT, userSeq);
        String storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(token);
    }

    /**
     * BLACK 리스트에서 토큰을 가져옵니다.
     *
     * @param userSeq 사용자 고유 식별자(PK)
     * @return BLACK 리스트에 저장된 토큰, 없으면 null
     */
    public String getTokenFromBlackList(Long userSeq) {
        String key = String.format(BLACK_KEY_FMT, userSeq);
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * JWT 토큰에서 Claims를 추출합니다.
     *
     * @param token JWT 토큰 문자열
     * @return Claims 객체
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 이메일 인증번호를 생성하고 전송한 후 Redis에 저장합니다.
     *
     * @param email 인증번호를 전송할 이메일 주소
     * @return 생성된 인증번호 (6자리 숫자)
     */
    public String sendAuthCode(String email) {
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);
        String subject = "Instrumer 이메일 인증번호";
        Context context = new Context();
        context.setVariable("authKey", authKey);
        String htmlContent = templateEngine.process("email-template", context);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

            String redisKey = String.format(EMAIL_AUTH_KEY_FMT, email);
            if (redisTemplate.hasKey(redisKey)) redisTemplate.delete(redisKey);

            redisTemplate.opsForValue().set(
                    redisKey,
                    authKey,
                    5,
                    TimeUnit.MINUTES
            );

            String savedAuthKey = redisTemplate.opsForValue().get(redisKey);
            if (savedAuthKey == null || !savedAuthKey.equals(authKey)) {
                throw new ServerException(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "인증번호 저장 중 오류가 발생했습니다."
                );
            }
        } catch (MessagingException e) {
            throw new ServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "이메일 전송 중 오류가 발생했습니다."
            );
        } catch (Exception e) {
            throw new ServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "이메일 인증번호 처리 중 오류가 발생했습니다."
            );
        }

        return authKey;
    }

    /**
     * 이메일과 인증번호를 검증합니다.
     *
     * @param email    이메일 주소
     * @param authCode 입력받은 인증번호
     * @return 검증 성공 시 true
     * @throws BadRequestException 인증번호가 일치하지 않거나 만료된 경우
     */
    public boolean verifyAuthCode(String email, String authCode) {
        String redisKey = String.format(EMAIL_AUTH_KEY_FMT, email);
        String savedAuthCode = redisTemplate.opsForValue().get(redisKey);

        if (savedAuthCode == null) {
            throw new BadRequestException(
                    HttpStatus.BAD_REQUEST.value(),
                    "인증번호가 만료되었거나 존재하지 않습니다."
            );
        }

        if (!savedAuthCode.equals(authCode)) {
            throw new BadRequestException(
                    HttpStatus.BAD_REQUEST.value(),
                    "인증번호가 일치하지 않습니다."
            );
        }

        redisTemplate.delete(redisKey);

        return true;
    }

    /**
     * S3 Presigned URL을 생성합니다.
     *
     * @param fileName   S3에 저장될 파일명
     * @param httpMethod HTTP 메서드 (PUT: 업로드, GET: 다운로드)
     * @param expiration 만료 시간 (분 단위)
     * @return 생성된 Presigned URL
     * @throws ServerException Presigned URL 생성 실패 시
     */
    public String generatePresignedUrl(String fileName, HttpMethod httpMethod, int expiration) {
        try {
            Date expirationDate = new Date();
            long expTimeMillis = expirationDate.getTime();
            expTimeMillis += 1000L * 60 * expiration; // 분을 밀리초로 변환
            expirationDate.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, fileName)
                            // .withMethod(HttpMethod)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expirationDate);

            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (Exception e) {
            throw new ServerException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Presigned URL 생성 중 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }
}
