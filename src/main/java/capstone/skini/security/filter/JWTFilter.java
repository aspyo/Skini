package capstone.skini.security.filter;

import capstone.skini.domain.user.entity.LoginType;
import capstone.skini.domain.user.entity.User;
import capstone.skini.security.dto.OAuth2UserDto;
import capstone.skini.security.jwt.JWTUtil;
import capstone.skini.security.user.CustomOAuth2User;
import capstone.skini.security.user.CustomPrincipal;
import capstone.skini.security.user.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            System.out.println("토큰이 만료되었습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("토큰이 만료되었습니다.");
            return;
        }

        if (!jwtUtil.getCategory(token).equals("access")) {
            System.out.println("토큰의 카테고리가 access 가 아닙니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("토큰의 카테고리가 access 가 아닙니다.");
            return;
        }

        String username = jwtUtil.getUsername(token);
        String loginId = jwtUtil.getLoginId(token);
        String role = jwtUtil.getRole(token);
        String loginType = jwtUtil.getLoginType(token);

        CustomPrincipal customPrincipal = null;

        if (jwtUtil.isOAuth2(token)) {
            OAuth2UserDto oAuth2UserDto = new OAuth2UserDto();
            oAuth2UserDto.setName(username);
            oAuth2UserDto.setSocialId(loginId);
            oAuth2UserDto.setRole(role);
            oAuth2UserDto.setLoginType(loginType);
            customPrincipal = new CustomOAuth2User(oAuth2UserDto);
        }else{
            User user = User.builder()
                    .username(username)
                    .loginId(loginId)
                    .role(role)
                    .loginType(LoginType.OUR)
                    .build();
            customPrincipal = new CustomUserDetails(user);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customPrincipal, null, customPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
