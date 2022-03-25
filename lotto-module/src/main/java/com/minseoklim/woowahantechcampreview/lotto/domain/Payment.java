package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Payment {
    static final BigDecimal LOTTO_PRICE = BigDecimal.valueOf(1000);
    static final String PAYMENT_ERR_MSG = "지불 금액은 " + LOTTO_PRICE.intValue() + "의 배수이어야 합니다.";

    @Column(nullable = false, name = "payment")
    private BigDecimal value;

    Payment(final int payment) {
        this.value = BigDecimal.valueOf(payment);
        validate();
    }

    private void validate() {
        if (value.compareTo(LOTTO_PRICE) < 0 || !value.remainder(LOTTO_PRICE).equals(BigDecimal.ZERO)) {
            throw new BadRequestException(PAYMENT_ERR_MSG);
        }
    }

    int calculateMaxLottoCount() {
        return value.divide(LOTTO_PRICE, RoundingMode.FLOOR).intValue();
    }
}
