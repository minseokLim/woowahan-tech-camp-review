package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.WinningNumbers.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class WinningNumbersTest {
    @Test
    @DisplayName("당첨 번호와 보너스 번호가 겹칠 때 예외 발생")
    void createByInvalidBonusNumber() {
        assertThatThrownBy(() -> new WinningNumbers(Set.of(1, 2, 3, 4, 5, 6), 6))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(INVALID_BONUS_NUMBER_ERR_MSG);
    }
}
