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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import com.minseoklim.woowahantechcampreview.AcceptanceTest;
import com.minseoklim.woowahantechcampreview.auth.acceptance.AuthAcceptanceTest;
import com.minseoklim.woowahantechcampreview.util.RequestUtil;

@SuppressWarnings("unchecked")
public class UserAcceptanceTest extends AcceptanceTest {
    public static final String USER_FILE_PATH1 = "json/user/user1.json";
    public static final String USER_FILE_PATH2 = "json/user/user2.json";
    public static final String INVALID_USERS_FILE_PATH = "json/user/invalidUsers.json";

    @Test
    void 사용자_관리() throws IOException {
        // given
        final var user = readJsonFile(USER_FILE_PATH1, Map.class);

        // when
        final var createResponse = 사용자_생성_요청(user);

        // then
        사용자_생성됨(createResponse);

        // when
        final var listResponse = 사용자_목록_조회_요청(ADMIN_TOKEN);

        // then
        사용자_목록_조회됨(listResponse);

        // given
        final var createdUser = extractResponse(createResponse, Map.class);
        final var createdUserId = createdUser.get("id");

        // when
        final var getResponse = 사용자_조회_요청(createdUserId, ADMIN_TOKEN);

        // then
        사용자_조회됨(getResponse, createdUser);

        // given
        final var newUser = readJsonFile(USER_FILE_PATH2, Map.class);

        // when
        final var updateResponse = 사용자_수정_요청(createdUserId, newUser, ADMIN_TOKEN);

        // then
        사용자_수정됨(updateResponse, newUser);

        // when
        final var roleAddResponse = 사용자_권한_추가_요청(createdUserId, "ADMIN", ADMIN_TOKEN);

        // then
        사용자_권한_추가됨(roleAddResponse, "ADMIN");

        // when
        final var roleDeleteResponse = 사용자_권한_삭제_요청(createdUserId, "ADMIN", ADMIN_TOKEN);

        // then
        사용자_권한_삭제됨(roleDeleteResponse);

        // when
        final var deleteResponse = 사용자_삭제_요청(createdUserId, ADMIN_TOKEN);

        // then
        사용자_삭제됨(deleteResponse);
    }

    @Test
    void 내정보_관리() throws IOException {
        // given
        final var user = readJsonFile(USER_FILE_PATH1, Map.class);

        // when
        final var createResponse = 사용자_생성_요청(user);

        // then
        사용자_생성됨(createResponse);

        // given
        final String loginId = (String)user.get("loginId");
        final String password = (String)user.get("password");
        final var loginResponse = AuthAcceptanceTest.로그인_요청(loginId, password);
        final String accessToken = (String)extractResponse(loginResponse, Map.class).get("accessToken");

        // when
        final var getResponse = 내정보_조회_요청(accessToken);

        // then
        내정보_조회됨(getResponse, user);

        // given
        final var newUser = readJsonFile(USER_FILE_PATH2, Map.class);

        // when
        final var updateResponse = 내정보_수정_요청(newUser, accessToken);

        // then
        내정보_수정됨(updateResponse, newUser);

        // when
        final var deleteResponse = 내정보_삭제_요청(accessToken);

        // then
        내정보_삭제됨(deleteResponse);
    }

    @Test
    void 비밀번호_재설정() throws IOException {
        // given
        final var user = readJsonFile(USER_FILE_PATH1, Map.class);
        사용자_생성_요청(user);
        final String loginId = (String)user.get("loginId");
        final String email = (String)user.get("email");

        // when
        final var mailResponse = 비밀번호_재설정_이메일_전송_요청(loginId, email);

        // then
        비밀번호_재설정_이메일_전송됨(mailResponse);

        // given
        final var token = 토큰_추출("sentEmailText.txt");

        // when
        final var checkResponse = 토큰_유효성_검사(token);

        // then
        토큰_유효함(checkResponse);

        // when
        final var changePasswordResponse = 비밀번호_재설정_요청(token, "newPassword111");

        // then
        비밀번호_재설정됨(changePasswordResponse);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUsers")
    void 유효성검사(final Map<String, Object> invalidUser) {
        // when
        final var response = 사용자_생성_요청(invalidUser);

        // then
        사용자_생성_실패(response);
    }

    public static ExtractableResponse<Response> 사용자_생성_요청(final Map<String, Object> user) {
        return RequestUtil.post("/users", user);
    }

    private static void 사용자_생성됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotNull();
    }

    private static ExtractableResponse<Response> 사용자_목록_조회_요청(final String accessToken) {
        return RequestUtil.getWithAccessToken("/users", accessToken);
    }

    private static void 사용자_목록_조회됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);
    }

    private static ExtractableResponse<Response> 사용자_조회_요청(final Object id, final String accessToken) {
        return RequestUtil.getWithAccessToken("/users/{id}", accessToken, id);
    }

    private static void 사용자_조회됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatus(response, HttpStatus.OK);
        assertEqualsUser(response, expectedUser);
    }

    private static ExtractableResponse<Response> 사용자_수정_요청(
        final Object id,
        final Map<String, Object> newUser,
        final String accessToken
    ) {
        return RequestUtil.putWithAccessToken("/users/{id}", accessToken, newUser, id);
    }

    private static void 사용자_수정됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatus(response, HttpStatus.OK);
        assertEqualsUser(response, expectedUser);
    }

    private static ExtractableResponse<Response> 사용자_권한_추가_요청(
        final Object id,
        final String role,
        final String accessToken
    ) {
        return RequestUtil.patchWithAccessToken("/users/{id}/roles", accessToken, singletonMap("role", role), id);
    }

    private static void 사용자_권한_추가됨(final ExtractableResponse<Response> response, final String role) {
        assertHttpStatus(response, HttpStatus.OK);
        final List<Object> roles = response.jsonPath().getList("roles");
        assertThat(roles).contains(role);
    }

    private static ExtractableResponse<Response> 사용자_권한_삭제_요청(
        final Object id,
        final String role,
        final String accessToken
    ) {
        return RequestUtil.deleteWithAccessToken("/users/{id}/roles", accessToken, singletonMap("role", role), id);
    }

    private static void 사용자_권한_삭제됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.NO_CONTENT);
    }

    private static ExtractableResponse<Response> 사용자_삭제_요청(final Object id, final String accessToken) {
        return RequestUtil.deleteWithAccessToken("/users/{id}", accessToken, id);
    }

    private static void 사용자_삭제됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.NO_CONTENT);
    }

    private static ExtractableResponse<Response> 내정보_조회_요청(final String accessToken) {
        return RequestUtil.getWithAccessToken("/users/me", accessToken);
    }

    private static void 내정보_조회됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatus(response, HttpStatus.OK);
        assertEqualsUser(response, expectedUser);
    }

    private static ExtractableResponse<Response> 내정보_수정_요청(
        final Map<String, Object> newUser,
        final String accessToken
    ) {
        return RequestUtil.putWithAccessToken("/users/me", accessToken, newUser);
    }

    private static void 내정보_수정됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatus(response, HttpStatus.OK);
        assertEqualsUser(response, expectedUser);
    }

    private static ExtractableResponse<Response> 내정보_삭제_요청(final String accessToken) {
        return RequestUtil.deleteWithAccessToken("/users/me", accessToken);
    }

    private static void 내정보_삭제됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.NO_CONTENT);
    }

    private static void 사용자_생성_실패(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.BAD_REQUEST);
    }

    private static ExtractableResponse<Response> 비밀번호_재설정_이메일_전송_요청(final String loginId, final String email) {
        final Map<String, Object> param = new HashMap<>();
        param.put("loginId", loginId);
        param.put("email", email);
        param.put("uriToResetPassword", "https://minseoklim.com/reset-password");

        return RequestUtil.post("/users/send-email-to-reset-password", param);
    }

    private static void 비밀번호_재설정_이메일_전송됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);
    }

    private static String 토큰_추출(final String filePath) throws IOException {
        final String emailText = readFile(filePath);
        final Matcher tokenMatcher = Pattern.compile("token=([A-Za-z0-9+/]+)\\s*").matcher(emailText);

        if (tokenMatcher.find()) {
            return tokenMatcher.group(1);
        }

        throw new IllegalArgumentException("파일 내에 토큰이 없습니다.");
    }

    private static ExtractableResponse<Response> 토큰_유효성_검사(final String token) {
        return RequestUtil.get("/users/check-reset-password-token", singletonMap("token", token));
    }

    private static void 토큰_유효함(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);
    }

    private static ExtractableResponse<Response> 비밀번호_재설정_요청(final String token, final String password) {
        final Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("password", password);

        return RequestUtil.patch("/users/password", params);
    }

    private static void 비밀번호_재설정됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);
    }

    private static List<Map<String, Object>> provideInvalidUsers() throws IOException {
        return readJsonFile(INVALID_USERS_FILE_PATH, List.class);
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
