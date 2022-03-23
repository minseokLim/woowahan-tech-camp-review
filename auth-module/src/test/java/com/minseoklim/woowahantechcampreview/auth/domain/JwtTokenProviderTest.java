package com.minseoklim.woowahantechcampreview.auth.domain;

import static com.minseoklim.woowahantechcampreview.auth.domain.Role.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.Base64Utils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY =
        Base64Utils.encodeToString("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest".getBytes());
    private static final JwtTokenProvider TOKEN_PROVIDER =
        new JwtTokenProvider(SECRET_KEY, Long.MAX_VALUE / 2, Long.MAX_VALUE / 2);

    @Test
    void createAccessToken() {
        // given
        final var authorities = List.of(ADMIN, USER);
        final var authentication = new UsernamePasswordAuthenticationToken("principal", "credentials", authorities);

        // when, then
        assertThat(TOKEN_PROVIDER.createAccessToken(authentication)).isNotNull();
    }

    @Test
    void createRefreshToken() {
        // when
        final var token = TOKEN_PROVIDER.createRefreshToken();

        // then
        assertThat(token).isNotNull();
    }
}
