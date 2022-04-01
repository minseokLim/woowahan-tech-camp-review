package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Purchase.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class PurchaseTest {
    @Test
    @DisplayName("지불 금액에서 수동으로 입력된 로또의 수를 제외한 나머지에 해당하는 수만큼 자동으로 로또가 생성되는지 테스트")
    void create() {
        // given
        final List<Collection<Integer>> manualNumbers = List.of(Set.of(1, 2, 3, 4, 5, 6), Set.of(7, 8, 9, 10, 11, 12));

        // when
        final Purchase purchase = new Purchase(10000, new Round(1), 1L, manualNumbers);

        // then
        assertThat(purchase.getLottos()).filteredOn(Lotto::isAuto).hasSize(8);
    }

    @Test
    @DisplayName("지불 금액에 비해 입력되는 로또의 수가 많을 때 예외 발생")
    void createByTooManyLottos() {
        // given
        final List<Collection<Integer>> manualNumbers = List.of(Set.of(1, 2, 3, 4, 5, 6), Set.of(7, 8, 9, 10, 11, 12));

        // when, then
        assertThatThrownBy(() -> new Purchase(1000, new Round(1), 1L, manualNumbers))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(OVER_LOTTOS_ERR_MSG);
    }

    @Test
    @DisplayName("토요일 오후 8시 ~ 9시에는 구매 시 예외 발생")
    void createByInvalidCreatedDate() {
        // given (토요일 오후 8시)
        final LocalDateTime invalidCreatedDate = LocalDateTime.of(2022, 3, 26, 20, 0);
        final List<Collection<Integer>> manualNumbers = List.of(Set.of(1, 2, 3, 4, 5, 6));

        // when, then
        assertThatThrownBy(() -> new Purchase(10000, new Round(1), 1L, manualNumbers, invalidCreatedDate))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(PURCHASE_NOT_ALLOWED_ERR_MSG);
    }
}
