package capstone.skini.domain.user.service;

import capstone.skini.domain.user.dto.EditUserDto;
import capstone.skini.domain.user.dto.JoinDto;
import capstone.skini.domain.user.dto.UserDto;
import capstone.skini.domain.user.entity.LoginType;
import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public ResponseEntity<?> join(JoinDto joinDto) {
        try {
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
                    .loginType(LoginType.OUR)
                    .build();
            User savedUser = userRepository.save(newUser);

            return ResponseEntity.ok().build();
        } catch (EntityExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findById(Long id) {
        try {
            User findUser = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cannot find User By Id : " + id));
            return ResponseEntity.ok(new UserDto(findUser));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public User findByLoginId(String loginId) {
        return userRepository.findUserByLoginId(loginId);
    }

    public ResponseEntity<?> editUser(String loginId, EditUserDto editUserDto) {
        try {
            User findUser = userRepository.findUserByLoginId(loginId);
            if (findUser == null) {
                throw new EntityNotFoundException("cannot find User By LoginId : " + loginId);
            }
            findUser.changeInfo(editUserDto);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteUser(User user) {
        try{
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
