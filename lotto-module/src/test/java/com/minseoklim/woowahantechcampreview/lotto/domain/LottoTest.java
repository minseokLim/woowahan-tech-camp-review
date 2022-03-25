package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Numbers.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class LottoTest {
    @Test
    void create() {
        // when
        final Lotto lotto = new Lotto(List.of(6, 1, 3, 2, 4, 5), Type.MANUAL);

        // then
        assertThat(lotto.getNumbers()).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNumbers")
    @DisplayName("로또의 번호들이 1~45까지의 중복되지 않는 숫자 6개로 이루어져 있지 않을 때 예외 발생")
    void createByInvalidNumbers(final List<Integer> invalidNumbers) {
        // when, then
        assertThatThrownBy(() -> new Lotto(invalidNumbers, Type.MANUAL))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(INVALID_NUMBER_ERR_MSG);
    }

    private static Stream<List<Integer>> provideInvalidNumbers() {
        return Stream.of(
            List.of(1, 2, 3, 4, 5, 46),
            List.of(1, 2, 3, 4, 5, 5),
            List.of(1, 2, 3, 4, 5),
            List.of(1, 2, 3, 4, 5, 6, 7)
        );
    }
}
