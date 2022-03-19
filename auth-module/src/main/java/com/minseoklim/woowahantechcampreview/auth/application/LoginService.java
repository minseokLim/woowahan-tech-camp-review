package com.minseoklim.woowahantechcampreview.auth.application;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.minseoklim.woowahantechcampreview.auth.dto.LoginRequest;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenResponse;

@Service
public class LoginService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;

    public LoginService(
        final AuthenticationManagerBuilder authenticationManagerBuilder,
        final TokenService tokenService
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
    }

    public TokenResponse login(final LoginRequest loginRequest) {
        final AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        final Authentication authentication = authenticationManager.authenticate(loginRequest.toAuthentication());

        final TokenResponse tokenResponse = tokenService.createTokenResponse(authentication);
        tokenService.saveRefreshToken(authentication.getName(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }
}
