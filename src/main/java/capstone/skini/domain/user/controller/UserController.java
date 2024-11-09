package capstone.skini.domain.user.controller;

import capstone.skini.domain.refresh.service.RefreshTokenService;
import capstone.skini.domain.user.dto.JoinDto;
import capstone.skini.domain.user.service.UserService;
import capstone.skini.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        userService.join(joinDto);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

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

        refreshTokenService.deleteRefreshToken(refresh);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(null);
    }
}
