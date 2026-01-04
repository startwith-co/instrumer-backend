package instrumers.backend.log.service;

import instrumers.backend.log.dto.ExceptionLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExceptionLogService {
    
    @Value("${log.path:./logs}")
    private String logPath;
    
    @Value("${log.file.name:backend}")
    private String logFileName;

    // 로그 패턴: yyyy-MM-dd HH:mm:ss.SSS [thread] LEVEL logger - message
    // %-5level은 공백을 포함할 수 있으므로 공백 처리 필요
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}) \\[(.*?)\\] (ERROR|WARN|INFO|DEBUG|TRACE)\\s+(.*?) - (.*)"
    );
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    // RMI/JMX 관련 로거는 제외
    private static final List<String> EXCLUDED_LOGGERS = List.of(
        "sun.rmi",
        "javax.management",
        "java.rmi"
    );

    /**
     * 로그 파일에서 ERROR 레벨 로그를 읽어서 반환합니다.
     *
     * @param start 시작 인덱스 (0부터 시작)
     * @param end 종료 인덱스 (포함하지 않음)
     * @return 예외 로그 DTO 목록 (시간 내림차순 정렬)
     */
    public List<ExceptionLogDto> getAllExceptionLogEntity(int start, int end) {
        try {
            // 에러 로그 파일 우선 시도
            Path errorLogPath = Paths.get(logPath, logFileName + "-error.log");
            Path mainLogPath = Paths.get(logPath, logFileName + ".log");
            
            Path logFile = Files.exists(errorLogPath) ? errorLogPath : mainLogPath;
            
            if (!Files.exists(logFile)) {
                log.warn("로그 파일을 찾을 수 없습니다: {}", logFile);
                return Collections.emptyList();
            }

            // 파일을 역순으로 읽어서 최신 로그부터 가져오기
            List<String> allLines = Files.readAllLines(logFile);
            Collections.reverse(allLines); // 최신 로그가 먼저 오도록

            // ERROR 레벨만 필터링하고, RMI/JMX 로그 제외, 애플리케이션 로그만 포함
            List<ExceptionLogDto> errorLogs = allLines.stream()
                    .filter(line -> line.contains(" ERROR "))
                    .filter(this::isApplicationLog) // 애플리케이션 로그만
                    .filter(this::isNotExcludedLogger) // RMI/JMX 로그 제외
                    .map(this::parseLogLine)
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            // 페이징 적용
            int fromIndex = Math.min(start, errorLogs.size());
            int toIndex = Math.min(end, errorLogs.size());
            
            if (fromIndex >= errorLogs.size()) {
                return Collections.emptyList();
            }

            return errorLogs.subList(fromIndex, toIndex);

        } catch (IOException e) {
            log.error("로그 파일 읽기 실패", e);
            return Collections.emptyList();
        }
    }

    /**
     * 애플리케이션 로그인지 확인합니다.
     */
    private boolean isApplicationLog(String logLine) {
        // GlobalExceptionHandler에서 나온 로그는 항상 포함
        if (logLine.contains("GlobalExceptionHandler")) {
            return true;
        }
        // instrumers.backend 패키지의 로그만 포함
        // 로거 이름이 축약될 수 있으므로 (i.b.e.handler 등) 여러 패턴 확인
        return logLine.contains("instrumers.backend")
            || logLine.contains("i.b.e.")  // instrumers.backend.exception 축약
            || logLine.contains("i.b.s.")  // instrumers.backend.solution 축약
            || logLine.contains("i.b.u.")  // instrumers.backend.user 축약
            || logLine.contains("i.b.c.");  // instrumers.backend.common 축약
    }

    /**
     * 제외할 로거인지 확인합니다.
     */
    private boolean isNotExcludedLogger(String logLine) {
        return EXCLUDED_LOGGERS.stream()
                .noneMatch(logLine::contains);
    }

    /**
     * 로그 라인을 파싱하여 ExceptionLogDto로 변환합니다.
     * 
     * 로그 패턴: yyyy-MM-dd HH:mm:ss.SSS [thread] LEVEL logger - message
     */
    private ExceptionLogDto parseLogLine(String logLine) {
        try {
            Matcher matcher = LOG_PATTERN.matcher(logLine);
            if (!matcher.find()) {
                return null;
            }

            String dateTimeStr = matcher.group(1);
            String message = matcher.group(5);

            LocalDateTime createdAt = null;
            try {
                createdAt = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                log.warn("날짜 파싱 실패: {}", dateTimeStr);
            }

            // GlobalExceptionHandler의 로그 형식에 맞게 파싱
            // "예외 발생: uri=..., method=..., status=..., message=..."
            // "유효성 검증 실패: uri=..., method=..., message=..."
            // "예상치 못한 예외 발생: uri=..., method=..., message=..."
            int httpStatus = extractHttpStatus(message);
            String errorCode = "ERR_" + httpStatus;
            String requestUri = extractUri(message);
            String methodName = extractMethodName(message);
            String actualMessage = extractActualMessage(message);

            return new ExceptionLogDto(
                    createdAt,
                    httpStatus,
                    errorCode,
                    actualMessage,
                    requestUri,
                    message, // requestBody는 전체 메시지로
                    methodName
            );

        } catch (Exception e) {
            log.warn("로그 라인 파싱 실패: {}", logLine, e);
            return null;
        }
    }

    private int extractHttpStatus(String message) {
        // "status=500" 또는 "httpStatus: 500" 같은 패턴 찾기
        Pattern statusPattern = Pattern.compile("(?:status|httpStatus)[=:]\\s*(\\d{3})");
        Matcher matcher = statusPattern.matcher(message);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return 500; // 기본값
    }

    private String extractUri(String message) {
        // "uri=" 또는 "requestURI=" 같은 패턴 찾기
        Pattern uriPattern = Pattern.compile("(?:uri|requestURI)[=:]\\s*([^,\\s}]+)");
        Matcher matcher = uriPattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "-";
    }

    private String extractMethodName(String message) {
        // "method=" 또는 "methodName=" 패턴 찾기
        Pattern methodPattern = Pattern.compile("(?:method|methodName)[=:]\\s*([^,\\s}]+)");
        Matcher matcher = methodPattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return "-";
    }

    /**
     * 실제 예외 메시지를 추출합니다.
     * GlobalExceptionHandler의 로그 형식에서 message 부분만 추출
     */
    private String extractActualMessage(String message) {
        // "예외 발생: uri=..., method=..., status=..., message=실제메시지"
        Pattern messagePattern = Pattern.compile("(?:message|message=)[=:]\\s*([^,}]+?)(?:,|$)");
        Matcher matcher = messagePattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        
        // 패턴이 맞지 않으면 전체 메시지 반환
        return message;
    }
}
