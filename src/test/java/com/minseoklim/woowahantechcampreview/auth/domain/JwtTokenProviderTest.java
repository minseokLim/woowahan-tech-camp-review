package com.minseoklim.woowahantechcampreview.auth.domain;

import static com.minseoklim.woowahantechcampreview.user.domain.Role.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.Base64Utils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY =
        Base64Utils.encodeToString("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest".getBytes());
    private static final JwtTokenProvider TOKEN_PROVIDER = new JwtTokenProvider(SECRET_KEY, 3600000);

    @Test
    void createToken() {
        // given
        final var authorities = List.of(ADMIN.toGrantedAuthority(), USER.toGrantedAuthority());
        final var authentication = new UsernamePasswordAuthenticationToken("principal", "credentials", authorities);

        // when
        final var token = TOKEN_PROVIDER.createToken(authentication);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    void extractAuthentication() {
        // given
        final var authorities = List.of(ADMIN.toGrantedAuthority(), USER.toGrantedAuthority());
        final var authentication = new UsernamePasswordAuthenticationToken("principal", "credentials", authorities);
        final var token = TOKEN_PROVIDER.createToken(authentication);

        // when
        final var extractedAuthentication = TOKEN_PROVIDER.extractAuthentication(token);

        // then
        assertThat(authentication.getAuthorities()).containsAll(extractedAuthentication.getAuthorities());
    }

    @Test
    void validateToken() {
        // given
        final var authorities = List.of(ADMIN.toGrantedAuthority(), USER.toGrantedAuthority());
        final var authentication = new UsernamePasswordAuthenticationToken("principal", "credentials", authorities);
        final var token = TOKEN_PROVIDER.createToken(authentication);

        // when
        final boolean valid1 = TOKEN_PROVIDER.validateToken(token);

        // then
        assertThat(valid1).isTrue();

        // given
        final var invalidToken = "invalidToken";

        // when
        final boolean valid2 = TOKEN_PROVIDER.validateToken(invalidToken);

        // then
        assertThat(valid2).isFalse();
    }
}
