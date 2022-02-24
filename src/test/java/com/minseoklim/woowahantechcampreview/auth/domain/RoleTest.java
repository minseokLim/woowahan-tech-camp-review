package com.minseoklim.woowahantechcampreview.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RoleTest {
    @ParameterizedTest
    @CsvSource(value = {"ROLE_ADMIN:ADMIN", "ROLE_USER:USER"}, delimiter = ':')
    void of(final String role, final Role expectedRole) {
        assertThat(Role.of(role)).isEqualTo(expectedRole);
    }
}
