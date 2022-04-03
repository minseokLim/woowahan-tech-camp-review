package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Round.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class RoundTest {
    @Test
    @DisplayName("당첨 번호가 이미 입력된 회차에 또 당첨 번호를 설정하려 할 경우 예외 발생")
    void applyWinningNumbers() {
        // given
        final Round round = new Round(1);
        round.applyWinningNumbers(new WinningNumbers(Set.of(1, 2, 3, 4, 5, 6), 7));

        // when, then
        assertThatThrownBy(() -> round.applyWinningNumbers(new WinningNumbers(Set.of(7, 8, 9, 10, 11, 12), 13)))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(WINNING_NUMBERS_ERR_MSG);
    }
}
