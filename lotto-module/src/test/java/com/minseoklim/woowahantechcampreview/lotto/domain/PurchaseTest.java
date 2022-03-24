package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Purchase.*;
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
    @Test
    @DisplayName("지불 금액에서 수동으로 입력된 로또의 수를 제외한 나머지에 해당하는 수만큼 자동으로 로또가 생성되는지 테스트")
    void create() {
        // when
        final Purchase purchase = new Purchase(10000, 1, 1L, List.of(Set.of(1, 2, 3, 4, 5, 6)));

        // then
        assertThat(purchase.getLottos()).filteredOn(Lotto::isAuto).hasSize(9);
    }

    @ParameterizedTest
    @ValueSource(ints = {999, 1001})
    @DisplayName("지불 금액이 " + LOTTO_PRICE + "원 미만이거나 " + LOTTO_PRICE + "원으로 나누어 떨어지지 않는 경우 예외 발생")
    void createByInvalidPayment(final int invalidPayment) {
        // when, then
        assertThatThrownBy(() -> new Purchase(invalidPayment, 1, 1L, List.of(Set.of(1, 2, 3, 4, 5, 6))))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(PAYMENT_ERR_MSG);
    }

    @Test
    @DisplayName("지불 금액에 비해 입력되는 로또의 수가 많을 때 예외 발생")
    void createByTooManyManualLottos() {
        // when, then
        assertThatThrownBy(() ->
            new Purchase(1000, 1, 1L, List.of(Set.of(1, 2, 3, 4, 5, 6), Set.of(7, 8, 9, 10, 11, 12))))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(OVER_MANUAL_LOTTOS_ERR_MSG);
    }

    @Test
    @DisplayName("토요일 오후 8시 ~ 9시에는 구매 시 예외 발생")
    void createByInvalidCreatedDate() {
        // given (토요일 오후 8시)
        final LocalDateTime invalidCreatedDate = LocalDateTime.of(2022, 3, 26, 20, 0);

        // when, then
        assertThatThrownBy(() ->
            new Purchase(10000, 1, 1L, List.of(Set.of(1, 2, 3, 4, 5, 6)), invalidCreatedDate))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(PURCHASE_NOT_ALLOWED_ERR_MSG);
    }
}
