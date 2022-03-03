package com.minseoklim.woowahantechcampreview.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.minseoklim.woowahantechcampreview.auth.domain.RefreshToken;
import com.minseoklim.woowahantechcampreview.auth.domain.repository.RefreshTokenRepository;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenResponse;
import com.minseoklim.woowahantechcampreview.auth.util.JwtTokenProvider;

@Service
public class TokenService {
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenValidityInMilliseconds;

    public TokenService(
        final JwtTokenProvider tokenProvider,
        final RefreshTokenRepository refreshTokenRepository,
        @Value("${custom.jwt.refresh-token-validity-in-milliseconds}") final long refreshTokenValidityInMilliseconds
    ) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    public TokenResponse createTokenResponse(final Authentication authentication) {
        final String accessToken = tokenProvider.createAccessToken(authentication);
        final String refreshToken = tokenProvider.createRefreshToken();

        return new TokenResponse(accessToken, refreshToken);
    }

    public void saveRefreshToken(final String userId, final String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken, refreshTokenValidityInMilliseconds));
    }

    public void invalidateRefreshToken(final Long userId) {
        invalidateRefreshToken(userId.toString());
    }

    public void invalidateRefreshToken(final String userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
