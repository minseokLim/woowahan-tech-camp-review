package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;
import com.minseoklim.woowahantechcampreview.lotto.config.NumberConverter;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class WinningNumbers {
    static final int SIZE = 6;
    static final String INVALID_NUMBERS_ERR_MSG = "로또의 번호들은 1~45까지의 중복되지 않는 숫자 6개로 이루어져 있어야 합니다.";
    static final String INVALID_BONUS_NUMBER_ERR_MSG = "보너스 번호는 당첨 번호와 겹칠 수 없습니다.";

    @Convert(converter = NumberConverter.class)
    private List<Number> winningNumbers = new ArrayList<>();

    private Number bonusNumber;

    public WinningNumbers(final Collection<Integer> winningNumbers, final int bonusNumber) {
        this.winningNumbers = winningNumbers.stream()
            .distinct()
            .sorted()
            .map(Number::new)
            .collect(Collectors.toUnmodifiableList());

        this.bonusNumber = new Number(bonusNumber);

        validate();
    }

    private void validate() {
        if (winningNumbers.size() != SIZE) {
            throw new BadRequestException(INVALID_NUMBERS_ERR_MSG);
        }
        if (winningNumbers.contains(bonusNumber)) {
            throw new BadRequestException(INVALID_BONUS_NUMBER_ERR_MSG);
        }
    }
}
