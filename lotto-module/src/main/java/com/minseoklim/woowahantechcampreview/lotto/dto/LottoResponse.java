package com.minseoklim.woowahantechcampreview.lotto.dto;

import java.util.List;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Lotto;
import com.minseoklim.woowahantechcampreview.lotto.domain.Rank;
import com.minseoklim.woowahantechcampreview.lotto.domain.Type;

@Getter
public class LottoResponse {
    private final Long id;

    private final List<Integer> numbers;

    private final Type type;

    private final Rank rank;

    public LottoResponse(final Long id, final List<Integer> numbers, final Type type, final Rank rank) {
        this.id = id;
        this.numbers = numbers;
        this.type = type;
        this.rank = rank;
    }

    public static LottoResponse of(final Lotto lotto, final Rank rank) {
        return new LottoResponse(lotto.getId(), lotto.getNumbers().getValues(), lotto.getType(), rank);
    }
}
