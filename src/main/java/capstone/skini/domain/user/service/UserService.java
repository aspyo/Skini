package capstone.skini.domain.user.service;

import capstone.skini.domain.user.dto.JoinDto;
import capstone.skini.domain.user.entity.LoginType;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public User join(JoinDto joinDto) {

        // 아이디 중복 검증
        validateDuplicateLoginId(joinDto.getLoginId());

        // 비밀번호 중복 검증
        validateDuplicatePassword(joinDto.getPassword());

        // 중복 검증 통과
        User newUser = User.builder()
                .username(joinDto.getUsername())
                .loginId(joinDto.getLoginId())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .age(joinDto.getAge())
                .email(joinDto.getEmail())
                .gender(joinDto.getGender())
                .role("ROLE_USER")
                .loginType(LoginType.SOCIAL)
                .build();

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    private void validateDuplicateLoginId(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new EntityExistsException("중복된 로그인 아이디입니다.");
        }

    }

    private void validateDuplicatePassword(String password) {
        if (userRepository.existsByPassword(bCryptPasswordEncoder.encode(password))) {
            throw new EntityExistsException("중복된 패스워드입니다.");
        }
    }

}
