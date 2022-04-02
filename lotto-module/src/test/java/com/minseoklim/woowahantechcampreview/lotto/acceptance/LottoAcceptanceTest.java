package com.minseoklim.woowahantechcampreview.lotto.acceptance;

import static com.minseoklim.woowahantechcampreview.auth.acceptance.AuthAcceptanceTestFixture.*;
import static com.minseoklim.woowahantechcampreview.lotto.acceptance.LottoAcceptanceTestFixture.*;
import static com.minseoklim.woowahantechcampreview.user.acceptance.UserAcceptanceTestFixture.*;
import static com.minseoklim.woowahantechcampreview.util.TestUtil.*;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.minseoklim.woowahantechcampreview.AcceptanceTest;

class LottoAcceptanceTest extends AcceptanceTest {
    private String accessToken;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // given
        final String loginId = "test1234";
        final String password = "password1234";

        final var user = new HashMap<String, Object>();
        user.put("loginId", loginId);
        user.put("password", password);
        user.put("nickName", "테스트계정");
        user.put("email", "test@test.com");

        사용자_생성_요청(user);

        accessToken = 로그인_요청(loginId, password).jsonPath().get("accessToken");

        로또_회차_추가_요청(ADMIN_TOKEN);
    }

    @Test
    void 로또() {
        // given
        final int payment = 10000;
        final List<List<Integer>> manualNumbers = List.of(List.of(1, 2, 3, 4, 5, 6), List.of(45, 44, 43, 42, 41, 40));

        // when
        final var buyResponse = 로또_구매_요청(accessToken, payment, manualNumbers);

        // then
        로또_구입됨(buyResponse);

        // given
        final int round = 1;
        final List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        final int bonusNumber = 7;

        // when
        final var postWinningResultResponse = 관리자가_당첨번호_입력_요청(ADMIN_TOKEN, round, winningNumbers, bonusNumber);

        // then
        당첨번호_입력됨(postWinningResultResponse);

        // when
        final var winningResultResponse = 당첨결과_조회_요청(accessToken);

        // then
        당첨결과_조회됨(winningResultResponse);
    }
}
