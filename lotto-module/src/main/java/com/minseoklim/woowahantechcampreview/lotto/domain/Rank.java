package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.Arrays;

public enum Rank {
    FIRST(6),
    SECOND(5),
    THIRD(5),
    FOURTH(4),
    FIFTH(3),
    MISS(0),
    NOT_DRAWN(-1);

    private static final String COUNT_OF_MATCH_SIZE_ERR_MSG = "매칭된 숫자의 갯수가 0보다 작거나 6보다 클 수는 없습니다.";

    private final int countOfWinningNumberMatch;

    Rank(final int countOfWinningNumberMatch) {
        this.countOfWinningNumberMatch = countOfWinningNumberMatch;
    }

    public static Rank valueOf(final int countOfWinningNumberMatch, final boolean matchBonus) {
        validate(countOfWinningNumberMatch);

        if (countOfWinningNumberMatch == SECOND.countOfWinningNumberMatch && matchBonus) {
            return SECOND;
        }
        if (countOfWinningNumberMatch == THIRD.countOfWinningNumberMatch && !matchBonus) {
            return THIRD;
        }
        return Arrays.stream(values())
            .filter(rank -> rank.countOfWinningNumberMatch == countOfWinningNumberMatch)
            .findFirst()
            .orElse(MISS);
    }

    private static void validate(final int countOfWinningNumberMatch) {
        if (countOfWinningNumberMatch < 0 || countOfWinningNumberMatch > Numbers.SIZE) {
            throw new IllegalArgumentException(COUNT_OF_MATCH_SIZE_ERR_MSG);
        }
    }
}
