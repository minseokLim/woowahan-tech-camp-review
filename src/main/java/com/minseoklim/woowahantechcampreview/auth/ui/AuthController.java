package com.minseoklim.woowahantechcampreview.auth.ui;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.minseoklim.woowahantechcampreview.auth.domain.JwtTokenProvider;
import com.minseoklim.woowahantechcampreview.auth.dto.LoginRequest;
import com.minseoklim.woowahantechcampreview.auth.dto.LoginResponse;

@RestController
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;

    public AuthController(
        final AuthenticationManagerBuilder authenticationManagerBuilder,
        final JwtTokenProvider tokenProvider
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest loginRequest) {
        final AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        final Authentication authentication = authenticationManager.authenticate(loginRequest.toAuthentication());

        final String token = tokenProvider.createToken(authentication);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        return new ResponseEntity<>(new LoginResponse(token), httpHeaders, HttpStatus.OK);
    }
}
