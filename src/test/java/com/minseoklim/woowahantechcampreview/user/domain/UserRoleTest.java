package com.minseoklim.woowahantechcampreview.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.minseoklim.woowahantechcampreview.auth.domain.Role;

class UserRoleTest {
    @Test
    void equalsRole() {
        // given
        final var userRole = new UserRole(Role.USER);

        // when, then
        assertThat(userRole.equalsRole(Role.USER)).isTrue();
    }
}
