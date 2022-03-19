package com.minseoklim.woowahantechcampreview.auth.domain;

import org.springframework.stereotype.Component;

import com.minseoklim.woowahantechcampreview.auth.domain.repository.RefreshTokenRepository;
import com.minseoklim.woowahantechcampreview.auth.util.JwtTokenParser;
import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;

@Component
public class RefreshTokenValidator {
    private final JwtTokenParser tokenParser;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenValidator(
        final JwtTokenParser tokenParser,
        final RefreshTokenRepository refreshTokenRepository
    ) {
        this.tokenParser = tokenParser;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean validate(final String userId, final String refreshToken) {
        final RefreshToken savedRefreshToken = getSavedRefreshToken(userId);
        return tokenParser.validateRefreshToken(refreshToken) && savedRefreshToken.hasSameValue(refreshToken);
    }

    private RefreshToken getSavedRefreshToken(final String userId) {
        return refreshTokenRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("리프레쉬 토큰을 찾을 수 없습니다."));
    }
}
