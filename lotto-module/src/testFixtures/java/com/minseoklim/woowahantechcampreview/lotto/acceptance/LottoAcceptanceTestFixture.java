package com.minseoklim.woowahantechcampreview.lotto.acceptance;

import static com.minseoklim.woowahantechcampreview.util.TestUtil.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import com.minseoklim.woowahantechcampreview.util.RequestUtil;

public interface LottoAcceptanceTestFixture {
    static ExtractableResponse<Response> 로또_회차_추가_요청(final String accessToken) {
        return RequestUtil.postWithAccessToken("/lottos/rounds", accessToken, Collections.emptyMap());
    }

    static ExtractableResponse<Response> 로또_구매_요청(final Map<String, Object> lotto, final String accessToken) {
        return RequestUtil.postWithAccessToken("/lottos", accessToken, lotto);
    }

    static void 로또_구입됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_CREATED);
        assertThat(response.header("Location")).isNotNull();
    }

    static ExtractableResponse<Response> 관리자가_당첨번호_입력_요청(
        final Map<String, Object> winningResult,
        final String accessToken
    ) {
        return RequestUtil.postWithAccessToken("/winning-results", accessToken, winningResult);
    }

    static void 당첨번호_입력됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_CREATED);
        assertThat(response.header("Location")).isNotNull();
    }

    static ExtractableResponse<Response> 당첨결과_조회_요청(final String accessToken) {
        return RequestUtil.getWithAccessToken("/lottos/me", accessToken);
    }

    static void 당첨결과_조회됨(final ExtractableResponse<Response> response) {
        assertHttpStatusCode(response, HttpStatus.SC_OK);
    }
}
