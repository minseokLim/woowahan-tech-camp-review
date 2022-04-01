package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.WinningNumbers.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class WinningNumbersTest {
    @ParameterizedTest
    @MethodSource("provideInvalidNumbers")
    @DisplayName("당첨 번호가 중복되지 않는 숫자 6개로 이루어져 있지 않을 때 예외 발생")
    void createByInvalidWinningNumbers(final List<Integer> winningNumbers, final int bonusNumber) {
        assertThatThrownBy(() -> new WinningNumbers(winningNumbers, bonusNumber))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(INVALID_NUMBERS_ERR_MSG);
    }

    @Test
    @DisplayName("당첨 번호와 보너스 번호가 겹칠 때 예외 발생")
    void createByInvalidBonusNumber() {
        assertThatThrownBy(() -> new WinningNumbers(Set.of(1, 2, 3, 4, 5, 6), 6))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(INVALID_BONUS_NUMBER_ERR_MSG);
    }

    private static Stream<Arguments> provideInvalidNumbers() {
        return Stream.of(
            Arguments.of(List.of(1, 2, 3, 4, 5), 6),
            Arguments.of(List.of(1, 2, 3, 4, 5, 6, 7), 8)
        );
    }
}
