package capstone.skini.security.service;

import capstone.skini.domain.user.entity.User;
import capstone.skini.domain.user.repository.UserRepository;
import capstone.skini.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findUserByLoginId(username);
        if (findUser == null) {
            throw new UsernameNotFoundException("해당 로그인 아이디 " + username + " 는 없습니다.");
        }

        return new CustomUserDetails(findUser);
    }
}
