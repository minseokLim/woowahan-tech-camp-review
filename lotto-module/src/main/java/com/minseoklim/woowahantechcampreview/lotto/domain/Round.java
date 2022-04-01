package com.minseoklim.woowahantechcampreview.lotto.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int round;

    private WinningNumbers winningNumbers;

    public Round(final int round) {
        this.round = round;
    }

    int getRound() {
        return round;
    }
}
