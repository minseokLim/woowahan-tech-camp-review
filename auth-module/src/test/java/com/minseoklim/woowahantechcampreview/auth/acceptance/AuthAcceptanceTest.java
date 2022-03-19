package com.minseoklim.woowahantechcampreview.auth.acceptance;

import static com.minseoklim.woowahantechcampreview.auth.acceptance.AuthAcceptanceTestFixture.*;
import static com.minseoklim.woowahantechcampreview.user.acceptance.UserAcceptanceTestFixture.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.minseoklim.woowahantechcampreview.AcceptanceTest;

@ActiveProfiles("auth")
class AuthAcceptanceTest extends AcceptanceTest {
    private String loginId;
    private String password;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // given
        final var user = new HashMap<String, Object>();
        user.put("loginId", "test1234");
        user.put("password", "password1234");
        user.put("nickName", "테스트계정");
        user.put("email", "test@test.com");

        사용자_생성_요청(user);
        loginId = (String)user.get("loginId");
        password = (String)user.get("password");
    }

    @Test
    void 인증() {
        // when
        final var loginResponse = 로그인_요청(loginId, password);

        // then
        로그인됨(loginResponse);

        // given
        final var accessToken = (String)loginResponse.jsonPath().get("accessToken");
        final var refreshToken = (String)loginResponse.jsonPath().get("refreshToken");

        // when
        final var tokenResponse = 토큰_재발급_요청(accessToken, refreshToken);

        // then
        토큰_재발급됨(tokenResponse);
    }

    @Test
    void 존재하지_않는_사용자로_로그인() {
        // when
        final var response = 로그인_요청("wrong" + loginId, password);

        // then
        로그인_실패(response);
    }

    @Test
    void 비밀번호가_틀림() {
        // when
        final var response = 로그인_요청(loginId, "wrong" + password);

        // then
        로그인_실패(response);
    }
}
