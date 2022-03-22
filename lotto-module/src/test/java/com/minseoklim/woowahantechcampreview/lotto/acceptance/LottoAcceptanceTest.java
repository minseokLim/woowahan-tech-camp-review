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
    }

    @Test
    void 로또() {
        // given
        final var lotto = new HashMap<String, Object>();
        lotto.put("payment", 10000);
        lotto.put("manualLottos", List.of(List.of(1, 2, 3, 4, 5, 6), List.of(45, 44, 43, 42, 41, 40)));

        // when
        final var buyResponse = 로또_구매_요청(lotto, accessToken);

        // then
        로또_구입됨(buyResponse);

        // given
        final var winningResult = new HashMap<String, Object>();
        winningResult.put("round", 1);
        winningResult.put("winningNumbers", List.of(1, 2, 3, 4, 5, 6));
        winningResult.put("bonusNumber", 7);

        // when
        final var postWinningResultResponse = 관리자가_당첨번호_입력_요청(winningResult, ADMIN_TOKEN);

        // then
        당첨번호_입력됨(postWinningResultResponse);

        // when
        final var winningResultResponse = 당첨결과_조회_요청(accessToken);

        // then
        당첨결과_조회됨(winningResultResponse);
    }
}
