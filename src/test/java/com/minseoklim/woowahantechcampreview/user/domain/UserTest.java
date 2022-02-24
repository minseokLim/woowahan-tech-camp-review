package com.minseoklim.woowahantechcampreview.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.minseoklim.woowahantechcampreview.auth.domain.Role;
import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class UserTest {
    @Test
    void create() {
        // when
        final User user = new User("test1234", "password1234", "테스트계정", "test@test.com");

        // then
        assertThat(user).isNotNull();
    }

    @Test
    void update() {
        // given
        final User user = new User("test1234", "password1234", "테스트계정", "test@test.com");
        final User newUser = new User("test1234", "newPassword1234", "newNickNm", "new@test.com");

        // when
        user.update(newUser);

        // then
        assertThat(user.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(user.getNickName()).isEqualTo(newUser.getNickName());
        assertThat(user.getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
    @DisplayName("로그인 아이디를 수정 시도 시 예외 발생")
    void updateLoginId() {
        // given
        final User user = new User("test1234", "password1234", "테스트계정", "test@test.com");
        final User newUser = new User("mslim", "newPassword1234", "newNickNm", "new@test.com");

        // when, then
        assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> user.update(newUser));
    }

    @Test
    void delete() {
        // given
        final User user = new User("test1234", "password1234", "테스트계정", "test@test.com");

        // when
        user.delete();

        // then
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    void addRole() {
        // given
        final User user = new User("test1234", "password1234", "테스트계정", "test@test.com");

        // when
        user.addRole(Role.ADMIN);

        // then
        assertThat(user.getRoles()).contains(Role.ADMIN);
    }

    @Test
    void deleteRole() {
        // given
        final User user = new User("test1234", "password1234", "테스트계정", "test@test.com");
        user.addRole(Role.ADMIN);

        // when
        user.deleteRole(Role.ADMIN);

        // then
        assertThat(user.getRoles()).doesNotContain(Role.ADMIN);
    }
}
