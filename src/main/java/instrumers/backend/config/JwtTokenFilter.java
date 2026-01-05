package instrumers.backend.config;

import instrumers.backend.common.service.CommonService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final CommonService commonService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = commonService.parseToken(token);
            String type = claims.get("type", String.class);
            Long userSeq = ((Number) claims.get("userSeq")).longValue();
            String userType = claims.get("userType", String.class);

            if (!"ACCESS".equalsIgnoreCase(type)) {
                writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "ACCESS 토큰만 사용할 수 있습니다.");
                return;
            }

            if (commonService.isTokenInBlackList(userSeq, token)) {
                writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "로그아웃된 토큰입니다.");
                return;
            }

            if (!commonService.isTokenInWhiteList(userSeq, token)) {
                writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "화이트리스트에 없는 토큰입니다.");
                return;
            }

            // userType null 체크 (ACCESS 토큰에는 userType이 필수)
            if (userType == null || userType.trim().isEmpty()) {
                writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 사용자 타입입니다.");
                return;
            }

            // Request attribute 설정
            request.setAttribute("accessToken", token);
            request.setAttribute("type", type);
            request.setAttribute("userSeq", userSeq);
            request.setAttribute("userType", userType);

            // Spring Security Authentication 설정
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userType));
            Authentication auth = new UsernamePasswordAuthenticationToken(userSeq, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            response.setHeader("Authorization", "Bearer " + token);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 입니다.");
        } catch (SignatureException e) {
            writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "잘못된 JWT 입니다.");
        } catch (JwtException e) {
            writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰 처리 중 오류가 발생했습니다.");
        } catch (Exception e) {
            writeUnauthorized(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private void writeUnauthorized(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String body = String.format(
                "{\"httpStatus\": %d, \"message\": \"%s\"}",
                status, escapeJson(message)
        );
        response.getWriter().write(body);
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
