package capstone.skini.domain.refresh.repository;

import capstone.skini.domain.refresh.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByRefreshToken(String refreshToken);

    RefreshToken findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
