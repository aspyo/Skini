package capstone.skini.security.dto;

import lombok.Data;

@Data
public class OAuth2UserDto {
    private String socialId;
    private String name;
    private String role;
    private String loginType;
}
