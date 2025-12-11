package instrumers.backend.exception.code;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExceptionCodeMapper {
    private static final Map<String, String> BAD_REQUEST_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> CONFLICT_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> NOT_FOUND_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> SERVER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, String> UNAUTHORIZED_MAP = new ConcurrentHashMap<>();

    static {
        // BadRequestException
        BAD_REQUEST_MAP.put("요청 데이터 오류입니다.", "BAD_REQUEST_EXCEPTION_001");
        BAD_REQUEST_MAP.put("인증번호가 만료되었거나 존재하지 않습니다.", "BAD_REQUEST_EXCEPTION_010");
        BAD_REQUEST_MAP.put("인증번호가 일치하지 않습니다.", "BAD_REQUEST_EXCEPTION_011");

        // ConflictException
        CONFLICT_MAP.put("이미 회원가입된 사용자입니다.", "CONFLICT_EXCEPTION_001");

        // NotFoundException
        NOT_FOUND_MAP.put("존재하지 않는 회원입니다.", "NOT_FOUND_EXCEPTION_001");

        // ServerException
        SERVER_MAP.put("내부 서버 오류입니다.", "SERVER_EXCEPTION_001");
        SERVER_MAP.put("이메일 전송 중 오류가 발생했습니다.", "SERVER_EXCEPTION_006");
        SERVER_MAP.put("인증번호 저장 중 오류가 발생했습니다.", "SERVER_EXCEPTION_007");
        SERVER_MAP.put("이메일 인증번호 처리 중 오류가 발생했습니다.", "SERVER_EXCEPTION_008");

        // UnauthorizedException
        UNAUTHORIZED_MAP.put("만료된 JWT 입니다.", "UNAUTHORIZED_EXCEPTION_002");
    }

    public static String getCode(String message, ExceptionType type) {
        return switch (type) {
            case BAD_REQUEST -> BAD_REQUEST_MAP.getOrDefault(message, "BAD_REQUEST_EXCEPTION_예외코드 설정하세요.");
            case CONFLICT -> CONFLICT_MAP.getOrDefault(message, "CONFLICT_EXCEPTION_예외코드 설정하세요.");
            case NOT_FOUND -> NOT_FOUND_MAP.getOrDefault(message, "NOT_FOUND_EXCEPTION_예외코드 설정하세요.");
            case SERVER -> SERVER_MAP.getOrDefault(message, "SERVER_EXCEPTION_예외코드 설정하세요.");
            case UNAUTHORIZED -> UNAUTHORIZED_MAP.getOrDefault(message, "UNAUTHORIZED_EXCEPTION_예외코드 설정하세요.");
        };
    }

    public enum ExceptionType {
        BAD_REQUEST,
        CONFLICT,
        NOT_FOUND,
        SERVER,
        UNAUTHORIZED
    }
}