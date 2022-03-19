package com.minseoklim.woowahantechcampreview.user.acceptance;

import static com.minseoklim.woowahantechcampreview.util.TestUtil.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import com.minseoklim.woowahantechcampreview.util.RequestUtil;

public interface UserAcceptanceTestFixture {

    static ExtractableResponse<Response> 사용자_생성_요청(final Map<String, Object> user) {
        return RequestUtil.post("/users", user);
    }

    static void 사용자_생성됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_CREATED);
        assertThat(response.header("Location")).isNotNull();
    }

    static ExtractableResponse<Response> 사용자_목록_조회_요청(final String accessToken) {
        return RequestUtil.getWithAccessToken("/users", accessToken);
    }

    static void 사용자_목록_조회됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
    }

    static ExtractableResponse<Response> 사용자_조회_요청(final Object id, final String accessToken) {
        return RequestUtil.getWithAccessToken("/users/{id}", accessToken, id);
    }

    static void 사용자_조회됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
        assertEqualsUser(response, expectedUser);
    }

    static ExtractableResponse<Response> 사용자_수정_요청(
        final Object id,
        final Map<String, Object> newUser,
        final String accessToken
    ) {
        return RequestUtil.putWithAccessToken("/users/{id}", accessToken, newUser, id);
    }

    static void 사용자_수정됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
        assertEqualsUser(response, expectedUser);
    }

    static ExtractableResponse<Response> 사용자_권한_추가_요청(
        final Object id,
        final String role,
        final String accessToken
    ) {
        return RequestUtil.patchWithAccessToken("/users/{id}/roles", accessToken, singletonMap("role", role), id);
    }

    static void 사용자_권한_추가됨(final ExtractableResponse<Response> response, final String role) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
        final List<Object> roles = response.jsonPath().getList("roles");
        assertThat(roles).contains(role);
    }

    static ExtractableResponse<Response> 사용자_권한_삭제_요청(
        final Object id,
        final String role,
        final String accessToken
    ) {
        return RequestUtil.deleteWithAccessToken("/users/{id}/roles", accessToken, singletonMap("role", role), id);
    }

    static void 사용자_권한_삭제됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_NO_CONTENT);
    }

    static ExtractableResponse<Response> 사용자_삭제_요청(final Object id, final String accessToken) {
        return RequestUtil.deleteWithAccessToken("/users/{id}", accessToken, id);
    }

    static void 사용자_삭제됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_NO_CONTENT);
    }

    static ExtractableResponse<Response> 내정보_조회_요청(final String accessToken) {
        return RequestUtil.getWithAccessToken("/users/me", accessToken);
    }

    static void 내정보_조회됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
        assertEqualsUser(response, expectedUser);
    }

    static ExtractableResponse<Response> 내정보_수정_요청(
        final Map<String, Object> newUser,
        final String accessToken
    ) {
        return RequestUtil.putWithAccessToken("/users/me", accessToken, newUser);
    }

    static void 내정보_수정됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
        assertEqualsUser(response, expectedUser);
    }

    static ExtractableResponse<Response> 내정보_삭제_요청(final String accessToken) {
        return RequestUtil.deleteWithAccessToken("/users/me", accessToken);
    }

    static void 내정보_삭제됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_NO_CONTENT);
    }

    static void 사용자_생성_실패(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_BAD_REQUEST);
    }

    static ExtractableResponse<Response> 비밀번호_재설정_이메일_전송_요청(final String loginId, final String email) {
        final Map<String, Object> param = new HashMap<>();
        param.put("loginId", loginId);
        param.put("email", email);
        param.put("uriToResetPassword", "https://minseoklim.com/reset-password");

        return RequestUtil.post("/users/send-email-to-reset-password", param);
    }

    static void 비밀번호_재설정_이메일_전송됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
    }

    static String 토큰_추출(final String filePath) throws IOException {
        final String emailText = readFile(filePath);
        final Matcher tokenMatcher = Pattern.compile("token=([A-Za-z0-9+/]+)\\s*").matcher(emailText);

        if (tokenMatcher.find()) {
            return tokenMatcher.group(1);
        }

        throw new IllegalArgumentException("파일 내에 토큰이 없습니다.");
    }

    static ExtractableResponse<Response> 토큰_유효성_검사(final String token) {
        return RequestUtil.get("/users/check-reset-password-token", singletonMap("token", token));
    }

    static void 토큰_유효함(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
    }

    static ExtractableResponse<Response> 비밀번호_재설정_요청(final String token, final String password) {
        final Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("password", password);

        return RequestUtil.patch("/users/password", params);
    }

    static void 비밀번호_재설정됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
    }

    private static void assertEqualsUser(
        final ExtractableResponse<Response> response,
        final Map<String, Object> expectedUser
    ) {
        final var actualUser = extractResponse(response, Map.class);
        assertThat(actualUser).containsEntry("loginId", expectedUser.get("loginId"));
        assertThat(actualUser).containsEntry("nickName", expectedUser.get("nickName"));
        assertThat(actualUser).containsEntry("email", expectedUser.get("email"));
    }
}
