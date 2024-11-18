package capstone.skini.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface CustomPrincipal extends UserDetails, OAuth2User {
    String getLoginId();
}
