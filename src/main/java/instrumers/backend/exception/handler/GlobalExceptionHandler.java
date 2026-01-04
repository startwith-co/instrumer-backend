package instrumers.backend.exception.handler;

import instrumers.backend.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler({ServerException.class, BadRequestException.class, NotFoundException.class, UnauthorizedException.class, ConflictException.class})
    public ResponseEntity<ErrorResponse> handleCustomException(final RuntimeException exception, final HttpServletRequest request) {

        if (exception instanceof CustomBaseException ex) {
            // 파일 로그에 저장 (logback이 자동으로 처리)
            log.error("uri={}, method={}, status={}, message={}",
                    request.getRequestURI(), request.getMethod(), ex.getHttpStatus(), ex.getMessage(), exception
            );

            return ResponseEntity.status(ex.getHttpStatus())
                    .body(new ErrorResponse(ex.getHttpStatus(), ex.getMessage()));
        }

        log.error(
                "uri={}, method={}",
                request.getRequestURI(), request.getMethod(), exception
        );

        return ResponseEntity.status(500).body(new ErrorResponse(500, "서버 내부 오류"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("요청 데이터 검증에 실패했습니다.");

        // 파일 로그에 저장 (logback이 자동으로 처리)
        log.error(
                "uri={}, method={}, message={}",
                request.getRequestURI(), request.getMethod(), errorMessage, exception
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception, final HttpServletRequest request) {
        // 파일 로그에 저장 (logback이 자동으로 처리)
        log.error(
                "uri={}, method={}, message={}",
                request.getRequestURI(), request.getMethod(), exception.getMessage(), exception
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final int httpStatus;
        private final String message;
    }
}
