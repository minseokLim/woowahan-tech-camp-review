package com.minseoklim.woowahantechcampreview.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void create() {
        // when
        final User user = new User("testId", "password123", "테스트계정", "test@test.com");

        // then
        assertThat(user).isNotNull();
    }
}
