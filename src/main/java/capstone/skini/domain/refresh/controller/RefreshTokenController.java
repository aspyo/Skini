package capstone.skini.domain.refresh.controller;

import capstone.skini.domain.refresh.service.RefreshTokenService;
import capstone.skini.domain.user.entity.LoginType;
import capstone.skini.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JWTUtil jwtUtil;

    /**
     * 리프레쉬 토큰으로 JWT 토큰 재발행
     */
    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refresh = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return ResponseEntity.badRequest().body("refresh Token 이 없습니다.");
        }

        if (!jwtUtil.getCategory(refresh).equals("refresh")) {
            return ResponseEntity.badRequest().body("토큰의 카테고리가 refresh 가 아닙니다.");
        }

        if (jwtUtil.isExpired(refresh)) {
            return ResponseEntity.badRequest().body("refresh Token 이 만료되었습니다.");
        }

        if (!refreshTokenService.existRefreshToken(refresh)) {
            return ResponseEntity.badRequest().body("해당 refresh Token 이 DB에 저장되어있지 않습니다..");
        }

        LoginType loginType = null;
        if (jwtUtil.isOAuth2(refresh)) {
            loginType = LoginType.SOCIAL;
        }else{
            loginType = LoginType.OUR;
        }

        String newJWT = jwtUtil.createJwt("access", jwtUtil.getUsername(refresh), jwtUtil.getRole(refresh), loginType,1000 * 60 * 60L);
        response.setHeader("jwt", newJWT);
        return ResponseEntity.ok(null);

    }
}
