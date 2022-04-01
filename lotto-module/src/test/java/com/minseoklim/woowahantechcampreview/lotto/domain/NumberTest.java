package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Number.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class NumberTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 46})
    @DisplayName("로또의 번호는 1~45까지의 숫자만 가능하다. 아니면 예외 발생")
    void createByInvalidNumber(final int invalidNumber) {
        assertThatThrownBy(() -> new Number(invalidNumber))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(INVALID_NUMBER_ERR_MSG);
    }
}
