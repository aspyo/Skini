package capstone.skini.domain.refresh.controller;

import capstone.skini.domain.refresh.entity.RefreshToken;
import capstone.skini.domain.refresh.repository.RefreshTokenRepository;
import capstone.skini.domain.refresh.service.RefreshTokenService;
import capstone.skini.domain.user.entity.LoginType;
import capstone.skini.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    /**
     * 리프레쉬 토큰으로 JWT 토큰 재발행
     */
    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = request.getHeader("refresh");
        String validation = validateRefreshToken(refresh);

        if (!validation.equals("true")) {
            return ResponseEntity.badRequest().body(validation);
        }

        if (!refreshTokenService.existRefreshToken(refresh)) {
            return ResponseEntity.badRequest().body("해당 refresh Token 이 DB에 저장되어있지 않습니다..");
        }

        LoginType loginType = null;
        if (jwtUtil.isOAuth2(refresh)) {
            loginType = LoginType.SOCIAL;
        } else {
            loginType = LoginType.OUR;
        }

        String newJWT = jwtUtil.createJwt("access", jwtUtil.getUsername(refresh), jwtUtil.getRole(refresh), loginType, 1000 * 60 * 60L);
        response.setHeader("jwt", newJWT);
        return ResponseEntity.ok(null);

    }

    /**
     * 소셜로그인일 경우 쿠키값의 refresh 으로 새로운 jwt, refresh 토큰 발급하여 응답헤더로 전달
     */
    @GetMapping("/oauth2token")
    public ResponseEntity<?> oauth2Token(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refresh = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        String validation = validateRefreshToken(refresh);

        if (!validation.equals("true")) {
            return ResponseEntity.badRequest().body(validation);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // JWT 토큰 생성(유효기간 = 1시간)
        String jwt = jwtUtil.createJwt("access", username, role, LoginType.SOCIAL, 1000 * 60 * 60L);

        // Refresh 토큰 생성(유효기간 = 7일)
        String refreshToken = jwtUtil.createJwt("refresh", username, role, LoginType.SOCIAL, 1000 * 60 * 60 * 24 * 7L);

        //Refresh 토큰 저장
        addRefreshToken(username,refreshToken,86400000L);

        //쿠키에서 Refresh 토큰 제거
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        //토큰 발급 및 응답 설정
        response.setHeader("jwt", jwt);
        response.setHeader("refresh", refreshToken);

        return ResponseEntity.ok("소셜로그인에서 토큰발급 성공");
    }

    private void addRefreshToken(String username, String refresh, long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .username(username)
                .refreshToken(refresh)
                .expiration(date.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    private String validateRefreshToken(String refresh) {
        if (refresh == null) {
            return "refresh Token 이 없습니다.";
        }

        if (!jwtUtil.getCategory(refresh).equals("refresh")) {
            return "토큰의 카테고리가 refresh 가 아닙니다.";
        }

        if (jwtUtil.isExpired(refresh)) {
            return "refresh Token 이 만료되었습니다.";
        }

        return "true";
    }
}
