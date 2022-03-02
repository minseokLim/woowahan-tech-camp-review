package com.minseoklim.woowahantechcampreview.auth.acceptance;

import static com.minseoklim.woowahantechcampreview.util.TestUtil.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import com.minseoklim.woowahantechcampreview.AcceptanceTest;
import com.minseoklim.woowahantechcampreview.user.acceptance.UserAcceptanceTest;
import com.minseoklim.woowahantechcampreview.util.RequestUtil;

@SuppressWarnings("unchecked")
public class AuthAcceptanceTest extends AcceptanceTest {
    private String loginId;
    private String password;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // given
        final var user = readJsonFile(UserAcceptanceTest.USER_FILE_PATH1, Map.class);
        UserAcceptanceTest.사용자_생성_요청(user);
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

    public static ExtractableResponse<Response> 로그인_요청(final String loginId, final String password) {
        final Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("loginId", loginId);
        bodyParam.put("password", password);

        return RequestUtil.post("/login", bodyParam);
    }

    private static void 로그인됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);
    }

    private static ExtractableResponse<Response> 토큰_재발급_요청(final String accessToken, final String refreshToken) {
        final Map<String, Object> bodyParam = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        return RequestUtil.post("/refresh-token", bodyParam);
    }

    private static void 토큰_재발급됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);

        final var newAccessToken = response.jsonPath().get("accessToken");
        final var newRefreshToken = response.jsonPath().get("refreshToken");
        assertThat(newAccessToken).isNotNull();
        assertThat(newRefreshToken).isNotNull();
    }

    private static void 로그인_실패(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.UNAUTHORIZED);
    }
}
