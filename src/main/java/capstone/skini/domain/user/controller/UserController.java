package capstone.skini.domain.user.controller;

import capstone.skini.domain.refresh.service.RefreshTokenService;
import capstone.skini.domain.user.dto.EditUserDto;
import capstone.skini.domain.user.dto.JoinDto;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.service.UserService;
import capstone.skini.security.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        userService.join(joinDto);
        return ResponseEntity.ok(null);
    }

    /**
     * 로그아웃
     * 리프레쉬 토큰 검증후 검증에 성공하면 해당 리프레쉬 토큰 삭제
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        String refresh = request.getHeader("refresh");

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
        return ResponseEntity.ok("로그아웃 성공");
    }

    /**
     * 유저 정보 조회
     */
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getUserInfo(@PathVariable("user_id") Long userId) {
        return userService.findById(userId);
    }

    /**
     * 유저 정보 수정
     * 메일 인증 과정 구현후 추가해야함!!
     */
//    @PostMapping("/user/{user_id}")
//    public ResponseEntity<?> editUserInfo(@PathVariable("user_id") Long userId, @RequestBody EditUserDto editUserDto) {
//
//    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable("user_id") Long userId) {
        return userService.deleteUser(userId);
    }
}
