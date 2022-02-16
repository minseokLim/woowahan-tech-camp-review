package com.minseoklim.woowahantechcampreview.user.acceptance;

import static com.minseoklim.woowahantechcampreview.util.TestUtil.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import com.minseoklim.woowahantechcampreview.AcceptanceTest;
import com.minseoklim.woowahantechcampreview.util.RequestUtil;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("unchecked")
public class UserAcceptanceTest extends AcceptanceTest {
    public static final String USER_FILE_PATH1 = "json/user/user1.json";
    public static final String USER_FILE_PATH2 = "json/user/user2.json";
    public static final String INVALID_USERS_FILE_PATH = "json/user/invalidUsers.json";

    @Test
    void 사용자_관리() {
        // given
        final var user = readJsonFile(USER_FILE_PATH1, Map.class);

        // when
        final var createResponse = 사용자_생성_요청(user);

        // then
        사용자_생성됨(createResponse);

        // when
        final var listResponse = 사용자_목록_조회_요청();

        // then
        사용자_목록_조회됨(listResponse);

        // given
        final var createdUser = extractResponse(createResponse, Map.class);
        final var createdUserId = createdUser.get("id");

        // when
        final var getResponse = 사용자_조회_요청(createdUserId);

        // then
        사용자_조회됨(getResponse, createdUser);

        // given
        final var newUser = readJsonFile(USER_FILE_PATH2, Map.class);

        // when
        final var updateResponse = 사용자_수정_요청(createdUserId, newUser);

        // then
        사용자_수정됨(updateResponse, newUser);

        // when
        final var deleteResponse = 사용자_삭제_요청(createdUserId);

        // then
        사용자_삭제됨(deleteResponse);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUsers")
    void 유효성검사(final Map<String, Object> invalidUser) {
        // when
        final var response = 사용자_생성_요청(invalidUser);

        // then
        사용자_생성_실패(response);
    }

    private static ExtractableResponse<Response> 사용자_생성_요청(final Map<String, Object> user) {
        return RequestUtil.post("/users", user);
    }

    private static void 사용자_생성됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotNull();
    }

    private static ExtractableResponse<Response> 사용자_목록_조회_요청() {
        return RequestUtil.get("/users");
    }

    private static void 사용자_목록_조회됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.OK);
    }

    private static ExtractableResponse<Response> 사용자_조회_요청(final Object id) {
        return RequestUtil.get("/users/{id}", id);
    }

    private static void 사용자_조회됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatus(response, HttpStatus.OK);
        final var user = extractResponse(response, Map.class);
        assertThat(user).isEqualTo(expectedUser);
    }

    private static ExtractableResponse<Response> 사용자_수정_요청(final Object id, final Map<String, Object> newUser) {
        return RequestUtil.put("/users/{id}", newUser, id);
    }

    private static void 사용자_수정됨(final ExtractableResponse<Response> response, final Map<String, Object> expectedUser) {
        assertHttpStatus(response, HttpStatus.OK);
        final var user = extractResponse(response, Map.class);
        assertThat(user.get("loginId")).isEqualTo(expectedUser.get("loginId"));
        assertThat(user.get("nickName")).isEqualTo(expectedUser.get("nickName"));
        assertThat(user.get("email")).isEqualTo(expectedUser.get("email"));
    }

    private static ExtractableResponse<Response> 사용자_삭제_요청(final Object id) {
        return RequestUtil.delete("/users/{id}", id);
    }

    private static void 사용자_삭제됨(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.NO_CONTENT);
    }

    private static void 사용자_생성_실패(final ExtractableResponse<Response> response) {
        assertHttpStatus(response, HttpStatus.BAD_REQUEST);
    }

    private static List<Map<String, Object>> provideInvalidUsers() {
        return readJsonFile(INVALID_USERS_FILE_PATH, List.class);
    }
}
