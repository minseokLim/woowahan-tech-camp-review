package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import org.springframework.data.domain.Range;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;
import com.minseoklim.woowahantechcampreview.lotto.config.IntegerListConverter;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Numbers {
    private static final Range<Integer> RANGE = Range.closed(1, 45);
    private static final int SIZE = 6;

    static final String INVALID_NUMBER_ERR_MSG = "로또의 번호들은 1~45까지의 중복되지 않는 숫자 6개로 이루어져 있어야 합니다.";

    @Column(nullable = false, name = "numbers")
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> values = new ArrayList<>();

    Numbers(final Collection<Integer> numbers) {
        this.values = numbers.stream()
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        validate();
    }

    private void validate() {
        if (values.size() != SIZE) {
            throw new BadRequestException(INVALID_NUMBER_ERR_MSG);
        }
        if (!values.stream().allMatch(RANGE::contains)) {
            throw new BadRequestException(INVALID_NUMBER_ERR_MSG);
        }
    }

    List<Integer> getValues() {
        return Collections.unmodifiableList(values);
    }
}
