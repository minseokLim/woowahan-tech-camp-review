package com.minseoklim.woowahantechcampreview.auth.application;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.minseoklim.woowahantechcampreview.auth.domain.JwtTokenParser;
import com.minseoklim.woowahantechcampreview.auth.domain.RefreshTokenValidator;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenRequest;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenResponse;

@Service
public class AuthService {
    private final JwtTokenParser tokenParser;
    private final RefreshTokenValidator refreshTokenValidator;
    private final TokenService tokenService;

    public AuthService(
        final JwtTokenParser tokenParser,
        final RefreshTokenValidator refreshTokenValidator,
        final TokenService tokenService
    ) {
        this.tokenParser = tokenParser;
        this.refreshTokenValidator = refreshTokenValidator;
        this.tokenService = tokenService;
    }

    public TokenResponse refreshToken(final TokenRequest tokenRequest) {
        final String accessToken = tokenRequest.getAccessToken();
        final String refreshToken = tokenRequest.getRefreshToken();

        final Authentication authentication = tokenParser.extractAuthentication(accessToken);

        validateRefreshToken(authentication.getName(), refreshToken);

        final TokenResponse tokenResponse = tokenService.createTokenResponse(authentication);
        tokenService.saveRefreshToken(authentication.getName(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    private void validateRefreshToken(final String userId, final String refreshToken) {
        if (!refreshTokenValidator.validate(userId, refreshToken)) {
            // 유효하지 않은 리프레시 토큰이 들어왔을 경우, 토큰 값이 탈취되었다고 보고 해당 유저의 리프레쉬 토큰을 삭제한다.
            tokenService.invalidateRefreshToken(userId);
            throw new BadCredentialsException("리프레쉬 토큰이 유효하지 않습니다.");
        }
    }
}
