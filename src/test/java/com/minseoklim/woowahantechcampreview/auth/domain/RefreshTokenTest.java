package com.minseoklim.woowahantechcampreview.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefreshTokenTest {
    @Test
    void hasSameValue() {
        // given
        final var token1 = new RefreshToken("1", "value", 1L);

        // when, then
        assertThat(token1.hasSameValue("value")).isTrue();
    }
}
