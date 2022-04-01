package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;
import com.minseoklim.woowahantechcampreview.lotto.config.NumberConverter;
import com.minseoklim.woowahantechcampreview.lotto.config.NumbersConverter;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class WinningNumbers {
    static final String INVALID_BONUS_NUMBER_ERR_MSG = "보너스 번호는 당첨 번호와 겹칠 수 없습니다.";

    @Convert(converter = NumbersConverter.class)
    private Numbers winningNumbers;

    @Convert(converter = NumberConverter.class)
    private Number bonusNumber;

    public WinningNumbers(final Collection<Integer> winningNumbers, final int bonusNumber) {
        final List<Integer> numbers = winningNumbers.stream()
            .distinct()
            .sorted()
            .collect(Collectors.toUnmodifiableList());

        this.winningNumbers = new Numbers(numbers);
        this.bonusNumber = Number.of(bonusNumber);

        validate();
    }

    private void validate() {
        if (winningNumbers.contains(bonusNumber)) {
            throw new BadRequestException(INVALID_BONUS_NUMBER_ERR_MSG);
        }
    }
}
