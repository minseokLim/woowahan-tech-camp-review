package com.minseoklim.woowahantechcampreview.user.domain;

import static com.minseoklim.woowahantechcampreview.user.domain.ResetPasswordToken.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResetPasswordTokenTest {
    @Test
    void applyTokenToUriToResetPassword() {
        // given
        final var token = new ResetPasswordToken(1L, 1L);
        final var uriToResetPassword = "https://minseoklim.com/reset-password";

        // when
        final String actual = token.applyTokenToUriToResetPassword(uriToResetPassword);

        // then
        assertThat(actual).matches("https://minseoklim\\.com/reset-password\\?" + QUERY_PARAM_NAME + "=.+");
    }
}
