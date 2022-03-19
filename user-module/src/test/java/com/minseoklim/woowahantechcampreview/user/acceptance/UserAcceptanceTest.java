package com.minseoklim.woowahantechcampreview.user.acceptance;

import static com.minseoklim.woowahantechcampreview.auth.acceptance.AuthAcceptanceTestFixture.*;
import static com.minseoklim.woowahantechcampreview.user.acceptance.UserAcceptanceTestFixture.*;
import static com.minseoklim.woowahantechcampreview.util.TestUtil.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.ActiveProfiles;

import com.minseoklim.woowahantechcampreview.AcceptanceTest;

@ActiveProfiles({"user", "auth"})
class UserAcceptanceTest extends AcceptanceTest {
    private static final String INVALID_USERS_FILE_PATH = "json/invalidUsers.json";

    @Test
    void 사용자_관리() {
        // given
        final var user = new HashMap<String, Object>();
        user.put("loginId", "test1234");
        user.put("password", "password1234");
        user.put("nickName", "테스트계정");
        user.put("email", "test@test.com");

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
        final var newUser = new HashMap<String, Object>();
        newUser.put("loginId", "test1234");
        newUser.put("password", "newPassword1234");
        newUser.put("nickName", "newNickNm");
        newUser.put("email", "new@test.com");

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
    void 내정보_관리() {
        // given
        final var user = new HashMap<String, Object>();
        user.put("loginId", "test1234");
        user.put("password", "password1234");
        user.put("nickName", "테스트계정");
        user.put("email", "test@test.com");

        // when
        final var createResponse = 사용자_생성_요청(user);

        // then
        사용자_생성됨(createResponse);

        // given
        final String loginId = (String)user.get("loginId");
        final String password = (String)user.get("password");
        final var loginResponse = 로그인_요청(loginId, password);
        final String accessToken = (String)extractResponse(loginResponse, Map.class).get("accessToken");

        // when
        final var getResponse = 내정보_조회_요청(accessToken);

        // then
        내정보_조회됨(getResponse, user);

        // given
        final var newUser = new HashMap<String, Object>();
        newUser.put("loginId", "test1234");
        newUser.put("password", "newPassword1234");
        newUser.put("nickName", "newNickNm");
        newUser.put("email", "new@test.com");

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
        final String loginId = "test1234";
        final String email = "test@test.com";

        final var user = new HashMap<String, Object>();
        user.put("loginId", loginId);
        user.put("password", "password1234");
        user.put("nickName", "테스트계정");
        user.put("email", email);

        사용자_생성_요청(user);

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

    private static List<Map<String, Object>> provideInvalidUsers() throws IOException {
        return readJsonFile(INVALID_USERS_FILE_PATH, List.class);
    }
}
