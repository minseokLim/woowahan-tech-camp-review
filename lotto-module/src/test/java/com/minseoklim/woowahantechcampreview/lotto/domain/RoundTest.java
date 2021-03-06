package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Round.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class RoundTest {
    @Test
    void computeOpeningTime() {
        // given
        final LocalDateTime now = LocalDateTime.now();

        // when
        final LocalDateTime openingTime = Round.computeOpeningTime(now);

        // then
        assertThat(openingTime).isAfter(now);
        assertThat(openingTime.getDayOfWeek()).isEqualTo(OPENING_DAY_OF_WEEK);
        assertThat(openingTime.toLocalTime()).isEqualTo(OPENING_TIME);
    }

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

    @Test
    @DisplayName("당첨 번호 오픈 시간 이전이거나 당첨 번호가 아직 null일 경우 NOT_DRAWN이 반환되는지 테스트")
    void computeRank() {
        // given
        final LocalDateTime openingTime = LocalDate.of(2022, 1, 6).atStartOfDay();
        final Round round = new Round(1, openingTime);
        final Lotto lotto = new Lotto(Set.of(1, 2, 3, 4, 5, 6), Type.MANUAL);

        // when, then
        assertThat(round.computeRank(lotto, LocalDate.of(2022, 1, 31).atStartOfDay())).isEqualTo(Rank.NOT_DRAWN);

        // given
        round.applyWinningNumbers(new WinningNumbers(Set.of(1, 2, 3, 4, 5, 6), 7));

        // when, then
        assertThat(round.computeRank(lotto, LocalDate.of(2022, 1, 1).atStartOfDay())).isEqualTo(Rank.NOT_DRAWN);
    }
}
