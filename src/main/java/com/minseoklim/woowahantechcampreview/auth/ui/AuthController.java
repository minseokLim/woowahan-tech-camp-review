package com.minseoklim.woowahantechcampreview.auth.ui;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.minseoklim.woowahantechcampreview.auth.application.AuthService;
import com.minseoklim.woowahantechcampreview.auth.application.LoginService;
import com.minseoklim.woowahantechcampreview.auth.dto.LoginRequest;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenRequest;
import com.minseoklim.woowahantechcampreview.auth.dto.TokenResponse;

@RestController
public class AuthController {
    private final LoginService loginService;
    private final AuthService authService;

    public AuthController(final LoginService loginService, final AuthService authService) {
        this.loginService = loginService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody final LoginRequest loginRequest) {
        final TokenResponse tokenResponse = loginService.login(loginRequest);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(tokenResponse.getAccessToken());

        return new ResponseEntity<>(tokenResponse, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody final TokenRequest tokenRequest) {
        final TokenResponse tokenResponse = authService.refreshToken(tokenRequest);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(tokenResponse.getAccessToken());

        return new ResponseEntity<>(tokenResponse, httpHeaders, HttpStatus.OK);
    }
}
