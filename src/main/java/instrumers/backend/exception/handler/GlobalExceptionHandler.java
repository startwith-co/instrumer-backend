package instrumers.backend.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import instrumers.backend.exception.*;
import instrumers.backend.log.service.ExceptionLogService;
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

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionLogService exceptionLogService;

    @ExceptionHandler({ServerException.class, BadRequestException.class, NotFoundException.class, UnauthorizedException.class, ConflictException.class})
    public ResponseEntity<ErrorResponse> handleCustomException(final RuntimeException exception, final HttpServletRequest request) {

        if (exception instanceof CustomBaseException ex) {
            String methodName = ex.getStackTrace().length > 0
                    ? ex.getStackTrace()[0].toString()
                    : "UNKNOWN";

            String requestBody = getRequestBody(request);

            exceptionLogService.saveExceptionLogEntity(
                    ex.getHttpStatus(),
                    ex.getMessage(),
                    request.getRequestURI(),
                    methodName,
                    requestBody
            );

            return ResponseEntity.status(ex.getHttpStatus())
                    .body(new ErrorResponse(ex.getHttpStatus(), ex.getMessage()));
        }

        return ResponseEntity.status(500)
                .body(new ErrorResponse(500, "서버 내부 오류"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("요청 데이터 검증에 실패했습니다.");

        String methodName = exception.getStackTrace().length > 0
                ? exception.getStackTrace()[0].toString()
                : "UNKNOWN";

        String requestBody = getRequestBody(request);

        exceptionLogService.saveExceptionLogEntity(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                request.getRequestURI(),
                methodName,
                requestBody
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception, final HttpServletRequest request) {
        String methodName = exception.getStackTrace().length > 0
                ? exception.getStackTrace()[0].toString()
                : "UNKNOWN";

        String requestBody = getRequestBody(request);

        exceptionLogService.saveExceptionLogEntity(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                request.getRequestURI(),
                methodName,
                requestBody
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            String contentType = request.getContentType();
            ObjectMapper mapper = new ObjectMapper();

            if (contentType != null && contentType.contains("application/json")) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader reader = request.getReader()) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }

                String raw = sb.toString();
                if (raw.isBlank()) return "[본문 없음]";

                Map<String, Object> json = mapper.readValue(raw, Map.class);
                Object requestData = json.get("request");

                if (requestData instanceof String str) {
                    try {
                        Object nested = mapper.readValue(str, Object.class);
                        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nested);
                    } catch (Exception e) {
                        return str;
                    }
                }

                if (requestData != null) {
                    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestData);
                }

                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            } else {
                Map<String, String[]> paramMap = request.getParameterMap();
                if (paramMap.isEmpty()) return "[본문 없음]";

                Map<String, Object> resultMap = new LinkedHashMap<>();
                for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                    String key = entry.getKey();
                    String[] values = entry.getValue();
                    resultMap.put(key, values.length == 1 ? values[0] : Arrays.asList(values));
                }

                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultMap);
            }
        } catch (Exception e) {
            return "[본문 추출 실패]";
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final int httpStatus;
        private final String message;
    }
}
