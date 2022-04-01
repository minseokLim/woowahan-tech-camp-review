package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Payment.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class PaymentTest {
    @ParameterizedTest
    @ValueSource(ints = {999, 1001})
    @DisplayName("지불 금액이 1000원 미만이거나 1000원으로 나누어 떨어지지 않는 경우 예외 발생")
    void createByInvalidPayment(final int invalidPayment) {
        // when, then
        assertThatThrownBy(() -> new Payment(invalidPayment))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(PAYMENT_ERR_MSG);
    }

    @Test
    @DisplayName("이 지불 금액으로 살 수 있는 로또의 최대 수를 반환")
    void calculateMaxLottoCount() {
        // given
        final Payment payment = new Payment(7000);

        // when
        final int maxLottoCount = payment.calculateMaxLottoCount();

        // then
        assertThat(maxLottoCount).isEqualTo(7);
    }
}
