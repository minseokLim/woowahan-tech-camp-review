package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;
import com.minseoklim.woowahantechcampreview.lotto.config.NumberConverter;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Numbers {
    static final int SIZE = 6;
    static final String INVALID_NUMBERS_ERR_MSG = "로또의 번호들은 1~45까지의 중복되지 않는 숫자 6개로 이루어져 있어야 합니다.";

    @Column(nullable = false, name = "numbers")
    @Convert(converter = NumberConverter.class)
    private List<Number> values = new ArrayList<>();

    Numbers(final Collection<Integer> numbers) {
        this.values = numbers.stream()
            .distinct()
            .sorted()
            .map(Number::new)
            .collect(Collectors.toUnmodifiableList());

        validate();
    }

    private void validate() {
        if (values.size() != SIZE) {
            throw new BadRequestException(INVALID_NUMBERS_ERR_MSG);
        }
    }

    public List<Integer> getValues() {
        return values.stream()
            .map(Number::getValue)
            .collect(Collectors.toUnmodifiableList());
    }
}
