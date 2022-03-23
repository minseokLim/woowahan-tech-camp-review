package com.minseoklim.woowahantechcampreview.auth.domain;

import static com.minseoklim.woowahantechcampreview.auth.domain.Role.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.Base64Utils;

class JwtTokenParserTest {
    private static final String SECRET_KEY =
        Base64Utils.encodeToString("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest".getBytes());
    private static final JwtTokenProvider TOKEN_PROVIDER =
        new JwtTokenProvider(SECRET_KEY, Long.MAX_VALUE / 2, Long.MAX_VALUE / 2);
    private static final JwtTokenParser TOKEN_PARSER = new JwtTokenParser(SECRET_KEY);

    @Test
    void extractAuthentication() {
        // given
        final var authorities = List.of(ADMIN, USER);
        final var authentication = new UsernamePasswordAuthenticationToken("principal", "credentials", authorities);
        final var token = TOKEN_PROVIDER.createAccessToken(authentication);

        // when
        final var extractedAuthentication = TOKEN_PARSER.extractAuthentication(token);

        // then
        assertThat(authentication.getAuthorities()).containsAll(extractedAuthentication.getAuthorities());
    }

    @Test
    void validateAccessToken() {
        // given
        final var authorities = List.of(ADMIN, USER);
        final var authentication = new UsernamePasswordAuthenticationToken("principal", "credentials", authorities);
        final var token = TOKEN_PROVIDER.createAccessToken(authentication);

        // when, then
        assertThat(TOKEN_PARSER.validateAccessToken(token)).isTrue();

        // given
        final var invalidToken = "invalidToken";

        // when

        // when, then
        assertThat(TOKEN_PARSER.validateAccessToken(invalidToken)).isFalse();
    }

    @Test
    void validateRefreshToken() {
        // given
        final var token = TOKEN_PROVIDER.createRefreshToken();

        // when, then
        assertThat(TOKEN_PARSER.validateRefreshToken(token)).isTrue();

        // given
        final var invalidToken = "invalidToken";

        // when, then
        assertThat(TOKEN_PARSER.validateRefreshToken(invalidToken)).isFalse();
    }
}
