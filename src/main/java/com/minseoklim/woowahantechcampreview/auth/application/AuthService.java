package com.minseoklim.woowahantechcampreview.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.minseoklim.woowahantechcampreview.auth.domain.RefreshToken;
import com.minseoklim.woowahantechcampreview.auth.domain.RefreshTokenValidator;
import com.minseoklim.woowahantechcampreview.auth.domain.repository.RefreshTokenRepository;
import com.minseoklim.woowahantechcampreview.auth.dto.LoginRequest;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenRequest;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenResponse;
import com.minseoklim.woowahantechcampreview.auth.util.JwtTokenParser;
import com.minseoklim.woowahantechcampreview.auth.util.JwtTokenProvider;

@Service
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final JwtTokenParser tokenParser;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenValidator refreshTokenValidator;

    @Value("${custom.jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    public AuthService(
        final AuthenticationManagerBuilder authenticationManagerBuilder,
        final JwtTokenProvider tokenProvider,
        final JwtTokenParser tokenParser,
        final RefreshTokenRepository refreshTokenRepository,
        final RefreshTokenValidator refreshTokenValidator
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.tokenParser = tokenParser;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidator = refreshTokenValidator;
    }

    public TokenResponse login(final LoginRequest loginRequest) {
        final AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        final Authentication authentication = authenticationManager.authenticate(loginRequest.toAuthentication());

        final TokenResponse tokenResponse = createTokenResponse(authentication);
        saveRefreshToken(authentication.getName(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    public TokenResponse refreshToken(final TokenRequest tokenRequest) {
        final String accessToken = tokenRequest.getAccessToken();
        final String refreshToken = tokenRequest.getRefreshToken();

        final Authentication authentication = tokenParser.extractAuthentication(accessToken);

        if (!refreshTokenValidator.validate(authentication.getName(), refreshToken)) {
            // 유효하지 않은 리프레시 토큰이 들어왔을 경우, 토큰 값이 탈취되었다고 보고 해당 유저의 리프레쉬 토큰을 삭제한다.
            invalidateRefreshToken(authentication.getName());
            throw new BadCredentialsException("리프레쉬 토큰이 유효하지 않습니다.");
        }

        final TokenResponse tokenResponse = createTokenResponse(authentication);
        saveRefreshToken(authentication.getName(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    public void invalidateRefreshToken(final Long userId) {
        invalidateRefreshToken(userId.toString());
    }

    private TokenResponse createTokenResponse(final Authentication authentication) {
        final String accessToken = tokenProvider.createAccessToken(authentication);
        final String refreshToken = tokenProvider.createRefreshToken();

        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveRefreshToken(final String userId, final String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken, refreshTokenValidityInMilliseconds));
    }

    private void invalidateRefreshToken(final String userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
