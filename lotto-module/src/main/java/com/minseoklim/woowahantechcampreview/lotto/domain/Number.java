package com.minseoklim.woowahantechcampreview.lotto.domain;

import org.springframework.data.domain.Range;

import lombok.EqualsAndHashCode;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@EqualsAndHashCode
public class Number {
    static final int MIN = 1;
    static final int MAX = 45;
    static final Range<Integer> RANGE = Range.closed(MIN, MAX);

    static final String INVALID_NUMBER_ERR_MSG = "로또의 번호는 1~45까지의 숫자만 가능합니다.";

    private int value;

    public Number(final int value) {
        this.value = value;
        validate();
    }

    private void validate() {
        if (!RANGE.contains(value)) {
            throw new BadRequestException(INVALID_NUMBER_ERR_MSG);
        }
    }

    int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
