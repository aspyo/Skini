package capstone.skini.domain.refresh.service;

import capstone.skini.domain.refresh.entity.RefreshToken;
import capstone.skini.domain.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        if (refreshTokenRepository.existsByRefreshToken(refreshToken)) {
            refreshTokenRepository.deleteByRefreshToken(refreshToken);
        }
    }

    public boolean existRefreshToken(String refreshToken) {
        return refreshTokenRepository.existsByRefreshToken(refreshToken);
    }
}
