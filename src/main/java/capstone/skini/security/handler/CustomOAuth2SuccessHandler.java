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

    /**
     * 소셜로그인 성공시 refresh 토큰만 쿠키값으로 설정하여 클라이언트에게 특정 url로 리다이렉트하여 전달
     * 클라이언트는 해당 페이지에서 useEffect로 백엔드의 /api/oauth2token 으로 get요청
     * 이후 백엔드의 /api/oauth2token 컨트롤러에서 refresh 토큰 검증후 jwt토큰과 refresh 토큰 생성 및 응답헤더로 전달
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = oAuth2User.getName();
        String loginId = oAuth2User.getLoginId();

        Iterator<? extends GrantedAuthority> iterator = authentication.getAuthorities().iterator();
        String role = iterator.next().getAuthority();

        // Refresh 토큰 생성(유효기간 = 7일)
        String refresh = jwtUtil.createJwt("refresh", username, loginId, role, LoginType.SOCIAL, 1000 * 60 * 60 * 24 * 7L);

        //응답 설정
        response.addCookie(createCookie("refresh", refresh));

        /**
         * 나중에 리다이렉션 url 클라이언트 페이지로 수정해야함!!
         */
        //이후 클라이언트의 특정 url로 리다이렉션 ex) http://localhost:3000/token
        //클라이언트에선 해당 페이지에서 useEffect 같은 함수로 백엔드의 특정 url로 요청
        //그러면 서버에서 jwt, refresh 토큰 발급후 응답헤더로 전달 -> 클라이언트에서 로컬스토리지에 저장하여 사용
        response.sendRedirect("http://localhost:8080/api/oauth2token");

    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 3);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
