package com.minseoklim.woowahantechcampreview.lotto.dto;

import java.util.List;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Lotto;
import com.minseoklim.woowahantechcampreview.lotto.domain.Type;

@Getter
public class LottoResponse {
    private final Long id;

    private final List<Integer> numbers;

    private final Type type;

    public LottoResponse(final Long id, final List<Integer> numbers, final Type type) {
        this.id = id;
        this.numbers = numbers;
        this.type = type;
    }

    public static LottoResponse of(final Lotto lotto) {
        return new LottoResponse(lotto.getId(), lotto.getNumbers().getValues(), lotto.getType());
    }
}
