package capstone.skini.security.service;

import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.repository.UserRepository;
import capstone.skini.security.dto.OAuth2UserDto;
import capstone.skini.security.response.GoogleResponse;
import capstone.skini.security.response.NaverResponse;
import capstone.skini.security.response.OAuth2Response;
import capstone.skini.security.user.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // access token을 사용해 서버로부터 사용자 정보 가져옴

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{
            throw new IllegalStateException("[에러:CustomOAuth2UserService.loadUser] 등록되지 않은 소셜로그인 registration ID 입니다.");
        }

        // 소셜로그인 사용자는 아이디,비밀번호를 자체DB에서 관리하지 않으므로 소셜로그인 사용자를 구별하기 위한 아이디를 별도로 설정
        String socialId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        User findUser = userRepository.findUserByLoginId(socialId);

        // DB에 없으면 새로운 유저이므로 회원가입 진행
        if (findUser == null) {
            User newUser = User.builder()
                    .username(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .loginId(socialId)
                    .build();
            userRepository.save(newUser);

            OAuth2UserDto oAuth2UserDto = new OAuth2UserDto();
            oAuth2UserDto.setSocialId(socialId);
            oAuth2UserDto.setName(oAuth2Response.getName());
            oAuth2UserDto.setRole("ROLE_USER");

            return new CustomOAuth2User(oAuth2UserDto);
        }else{
            // DB에 이미 있는 소셜로그인 사용자이므로 회원가입 진행X
            OAuth2UserDto oAuth2UserDto = new OAuth2UserDto();
            oAuth2UserDto.setSocialId(findUser.getLoginId());
            oAuth2UserDto.setName(findUser.getUsername());
            oAuth2UserDto.setRole(findUser.getRole());

            return new CustomOAuth2User(oAuth2UserDto);
        }
    }
}
