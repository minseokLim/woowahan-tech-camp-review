package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Round {
    static final DayOfWeek OPENING_DAY_OF_WEEK = DayOfWeek.SATURDAY;
    static final LocalTime OPENING_TIME = LocalTime.of(21, 0, 0, 0);
    static final String WINNING_NUMBERS_ERR_MSG = "해당 회차의 당첨 번호는 이미 입력 되었습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private int round;

    private LocalDateTime winningNumberOpeningTime;

    private WinningNumbers winningNumbers;

    Round(final int round) {
        this(round, LocalDateTime.MAX);
    }

    public Round(final int round, final LocalDateTime winningNumberOpeningTime) {
        this.round = round;
        this.winningNumberOpeningTime = winningNumberOpeningTime;
    }

    public static LocalDateTime computeOpeningTime(final LocalDateTime now) {
        final DayOfWeek nowDayOfWeek = now.getDayOfWeek();
        final int additionalDays = OPENING_DAY_OF_WEEK.getValue() - nowDayOfWeek.getValue();

        if (nowDayOfWeek.compareTo(OPENING_DAY_OF_WEEK) > 0) {
            return now.plusDays(DayOfWeek.values().length + additionalDays).with(OPENING_TIME);
        }
        return now.plusDays(additionalDays).with(OPENING_TIME);
    }

    public void applyWinningNumbers(final WinningNumbers winningNumbers) {
        if (this.winningNumbers != null) {
            throw new BadRequestException(WINNING_NUMBERS_ERR_MSG);
        }
        this.winningNumbers = winningNumbers;
    }

    public Rank computeRank(final Lotto lotto, final LocalDateTime now) {
        if (now.isBefore(winningNumberOpeningTime) || winningNumbers == null) {
            return Rank.NOT_DRAWN;
        }
        return winningNumbers.computeRank(lotto);
    }

    public int getRound() {
        return round;
    }
}
