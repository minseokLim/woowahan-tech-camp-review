package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@EqualsAndHashCode
public class Numbers {
    static final int SIZE = 6;
    static final String INVALID_NUMBERS_ERR_MSG = "로또의 번호들은 1~45까지의 중복되지 않는 숫자 6개로 이루어져 있어야 합니다.";

    private final List<Number> values;

    public Numbers(final Collection<Integer> numbers) {
        this.values = numbers.stream()
            .distinct()
            .sorted()
            .map(Number::of)
            .collect(Collectors.toUnmodifiableList());

        validate();
    }

    private void validate() {
        if (values.size() != SIZE) {
            throw new BadRequestException(INVALID_NUMBERS_ERR_MSG);
        }
    }

    boolean contains(final Number number) {
        return values.contains(number);
    }

    public List<Integer> getValues() {
        return values.stream()
            .map(Number::getValue)
            .collect(Collectors.toUnmodifiableList());
    }
}
