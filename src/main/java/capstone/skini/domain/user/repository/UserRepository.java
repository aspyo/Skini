package capstone.skini.domain.user.repository;

import capstone.skini.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);

    boolean existsByPassword(String password);

    User findUserByLoginId(String loginId);

    Optional<User> findUserByUsername(String username);
}
