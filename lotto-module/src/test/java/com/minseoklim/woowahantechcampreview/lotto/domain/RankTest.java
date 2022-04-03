package com.minseoklim.woowahantechcampreview.lotto.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RankTest {
    @ParameterizedTest
    @CsvSource(value = {"6:true:FIRST", "5:true:SECOND", "5:false:THIRD", "2:true:MISS"}, delimiter = ':')
    void valueOf(final int countOfWinningNumberMatch, final boolean matchBonus, final Rank expectedRank) {
        // when
        final Rank rank = Rank.valueOf(countOfWinningNumberMatch, matchBonus);

        // then
        assertThat(rank).isEqualTo(expectedRank);
    }
}
