package com.minseoklim.woowahantechcampreview.lotto.domain;

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
    static final String WINNING_NUMBERS_ERR_MSG = "해당 회차의 당첨 번호는 이미 입력 되었습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private int round;

    private WinningNumbers winningNumbers;

    public Round(final int round) {
        this.round = round;
    }

    public void applyWinningNumbers(final WinningNumbers winningNumbers) {
        if (this.winningNumbers != null) {
            throw new BadRequestException(WINNING_NUMBERS_ERR_MSG);
        }
        this.winningNumbers = winningNumbers;
    }

    public int getRound() {
        return round;
    }
}
