package com.minseoklim.woowahantechcampreview.lotto.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LottoTest {
    @Test
    @DisplayName("이미 Purchase와의 연관관계가 맺어진 객체에 대해 또다시 연관관계를 맺으려 시도할 경우 예외 발생")
    void withPurchase() {
        // given
        final Set<Integer> manualNumber = Set.of(1, 2, 3, 4, 5, 6);
        final Lotto lotto = new Lotto(manualNumber, Type.MANUAL);
        lotto.withPurchase(new Purchase(1000, new Round(1), 1L, List.of(manualNumber)));

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> lotto.withPurchase(new Purchase(2000, new Round(2), 1L, List.of(manualNumber))));
    }
}
