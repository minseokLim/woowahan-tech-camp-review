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

    @Test
    void update() {
        // given
        final User user = new User("testId", "password123", "테스트계정", "test@test.com");
        final User other = new User("newId", "newPassword", "newNickName", "new@test.com");

        // when
        user.update(other);

        // then
        assertThat(user.getLoginId()).isEqualTo(other.getLoginId());
        assertThat(user.getPassword()).isEqualTo(other.getPassword());
        assertThat(user.getNickName()).isEqualTo(other.getNickName());
        assertThat(user.getEmail()).isEqualTo(other.getEmail());
    }
}
