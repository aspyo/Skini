package capstone.skini.security.user;

import capstone.skini.security.dto.OAuth2UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements CustomPrincipal{

    private final OAuth2UserDto oAuth2UserDto;

    @Override
    public String getLoginId() {
        return oAuth2UserDto.getSocialId();
    }

    @Override
    public String getName() {
        return oAuth2UserDto.getName();
    }

    @Override
    public String getUsername() {
        return oAuth2UserDto.getName();
    }

    public String getLoginType() {
        return oAuth2UserDto.getLoginType();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> oAuth2UserDto.getRole());
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
