package capstone.skini.security.handler;

import capstone.skini.domain.refresh.entity.RefreshToken;
import capstone.skini.domain.refresh.repository.RefreshTokenRepository;
import capstone.skini.domain.user.entity.LoginType;
import capstone.skini.security.jwt.JWTUtil;
import capstone.skini.security.user.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getName();

        Iterator<? extends GrantedAuthority> iterator = authentication.getAuthorities().iterator();
        String role = iterator.next().getAuthority();

        // JWT 토큰 생성(유효기간 = 1시간)
        String jwt = jwtUtil.createJwt("access", username, role, LoginType.SOCIAL, 1000 * 60 * 60L);

        // Refresh 토큰 생성(유효기간 = 7일)
        String refresh = jwtUtil.createJwt("refresh", username, role, LoginType.SOCIAL, 1000 * 60 * 60 * 24 * 7L);

        //Refresh 토큰 저장
        addRefreshToken(username, refresh,86400000L);

        //응답 설정
        response.setHeader("jwt", jwt);
        response.addCookie(createCookie("refresh", refresh));
        response.sendRedirect("http://localhost:8080/");

        System.out.println("refresh = " + refresh);
        System.out.println("jwt = " + jwt);
        System.out.println("성공!!");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setHttpOnly(true);

        return cookie;

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
}
