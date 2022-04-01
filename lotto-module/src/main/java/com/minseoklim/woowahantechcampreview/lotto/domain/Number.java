package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Range;

import lombok.EqualsAndHashCode;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@EqualsAndHashCode
public class Number {
    static final int MIN = 1;
    static final int MAX = 45;
    static final String INVALID_NUMBER_ERR_MSG = "로또의 번호는 1~45까지의 숫자만 가능합니다.";

    private static final Map<Integer, Number> VALUE_TO_NUMBER;

    private final int value;

    static {
        final Map<Integer, Number> cache = new HashMap<>();
        for (int i = MIN; i <= MAX; i++) {
            cache.put(i, new Number(i));
        }
        VALUE_TO_NUMBER = Collections.unmodifiableMap(cache);
    }

    private Number(final int value) {
        this.value = value;
    }

    public static Number of(final int number) {
        validate(number);
        return VALUE_TO_NUMBER.get(number);
    }

    private static void validate(final int number) {
        if (!Range.closed(MIN, MAX).contains(number)) {
            throw new BadRequestException(INVALID_NUMBER_ERR_MSG);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
