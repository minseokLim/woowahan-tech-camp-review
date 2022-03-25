package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Payment.*;
import static com.minseoklim.woowahantechcampreview.lotto.domain.Purchase.*;
import static com.minseoklim.woowahantechcampreview.lotto.domain.Type.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class PurchaseTest {
    @ParameterizedTest
    @ValueSource(ints = {999, 1001})
    @DisplayName("지불 금액이 1000원 미만이거나 1000원으로 나누어 떨어지지 않는 경우 예외 발생")
    void createByInvalidPayment(final int invalidPayment) {
        // given
        final List<Lotto> lottos = List.of(new Lotto(Set.of(1, 2, 3, 4, 5, 6), MANUAL));

        // when, then
        assertThatThrownBy(() -> new Purchase(invalidPayment, 1, 1L, lottos))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(PAYMENT_ERR_MSG);
    }

    @Test
    @DisplayName("지불 금액에 비해 입력되는 로또의 수가 많을 때 예외 발생")
    void createByTooManyLottos() {
        // given
        final List<Lotto> lottos =
            List.of(new Lotto(Set.of(1, 2, 3, 4, 5, 6), MANUAL), new Lotto(Set.of(7, 8, 9, 10, 11, 12), AUTO));

        // when, then
        assertThatThrownBy(() -> new Purchase(1000, 1, 1L, lottos))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(OVER_LOTTOS_ERR_MSG);
    }

    @Test
    @DisplayName("토요일 오후 8시 ~ 9시에는 구매 시 예외 발생")
    void createByInvalidCreatedDate() {
        // given (토요일 오후 8시)
        final LocalDateTime invalidCreatedDate = LocalDateTime.of(2022, 3, 26, 20, 0);
        final List<Lotto> lottos = List.of(new Lotto(Set.of(1, 2, 3, 4, 5, 6), MANUAL));

        // when, then
        assertThatThrownBy(() -> new Purchase(10000, 1, 1L, lottos, invalidCreatedDate))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(PURCHASE_NOT_ALLOWED_ERR_MSG);
    }
}
