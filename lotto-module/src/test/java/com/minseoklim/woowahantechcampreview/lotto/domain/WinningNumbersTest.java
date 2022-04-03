package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Rank.*;
import static com.minseoklim.woowahantechcampreview.lotto.domain.Type.*;
import static com.minseoklim.woowahantechcampreview.lotto.domain.WinningNumbers.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class WinningNumbersTest {

    @Test
    @DisplayName("당첨 번호와 보너스 번호가 겹칠 때 예외 발생")
    void createByInvalidBonusNumber() {
        assertThatThrownBy(() -> new WinningNumbers(Set.of(1, 2, 3, 4, 5, 6), 6))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(INVALID_BONUS_NUMBER_ERR_MSG);
    }

    @ParameterizedTest
    @MethodSource("provideLottoResults")
    void computeRank(final Lotto lotto, final Rank expectedRank) {
        // given
        final WinningNumbers winningNumbers = new WinningNumbers(Set.of(1, 2, 3, 4, 5, 6), 7);

        // when
        final Rank rank = winningNumbers.computeRank(lotto);

        // then
        assertThat(rank).isEqualTo(expectedRank);
    }

    private static Stream<Arguments> provideLottoResults() {
        return Stream.of(
            Arguments.of(new Lotto(Set.of(1, 2, 3, 4, 5, 6), MANUAL), FIRST),
            Arguments.of(new Lotto(Set.of(1, 2, 3, 4, 5, 7), MANUAL), SECOND),
            Arguments.of(new Lotto(Set.of(1, 2, 3, 4, 5, 8), MANUAL), THIRD),
            Arguments.of(new Lotto(Set.of(1, 2, 3, 4, 9, 10), MANUAL), FOURTH),
            Arguments.of(new Lotto(Set.of(1, 2, 3, 7, 8, 9), MANUAL), FIFTH),
            Arguments.of(new Lotto(Set.of(1, 2, 7, 8, 9, 10), MANUAL), MISS)
        );
    }
}
