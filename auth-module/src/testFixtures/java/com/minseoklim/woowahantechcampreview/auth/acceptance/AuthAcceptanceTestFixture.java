package com.minseoklim.woowahantechcampreview.auth.acceptance;

import static com.minseoklim.woowahantechcampreview.util.TestUtil.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import com.minseoklim.woowahantechcampreview.util.RequestUtil;

public interface AuthAcceptanceTestFixture {
    static ExtractableResponse<Response> 로그인_요청(final String loginId, final String password) {
        final Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("loginId", loginId);
        bodyParam.put("password", password);

        return RequestUtil.post("/login", bodyParam);
    }

    static void 로그인됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
    }

    static ExtractableResponse<Response> 토큰_재발급_요청(final String accessToken, final String refreshToken) {
        final Map<String, Object> bodyParam = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        return RequestUtil.post("/refresh-token", bodyParam);
    }

    static void 토큰_재발급됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);

        final var newAccessToken = response.jsonPath().get("accessToken");
        final var newRefreshToken = response.jsonPath().get("refreshToken");
        assertThat(newAccessToken).isNotNull();
        assertThat(newRefreshToken).isNotNull();
    }

    static void 로그인_실패(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_UNAUTHORIZED);
    }
}
