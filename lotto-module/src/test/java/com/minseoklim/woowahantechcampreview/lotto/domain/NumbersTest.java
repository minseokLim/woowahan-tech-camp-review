package com.minseoklim.woowahantechcampreview.lotto.domain;

import static com.minseoklim.woowahantechcampreview.lotto.domain.Numbers.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

class NumbersTest {
    @Test
    @DisplayName("로또 번호 생성 시, 오름차순으로 정렬되는지 테스트")
    void create() {
        // when
        final Numbers numbers = new Numbers(List.of(6, 1, 3, 2, 4, 5));

        // then
        assertThat(numbers.getValues()).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNumbers")
    @DisplayName("로또의 번호들이 중복되지 않는 숫자 6개로 이루어져 있지 않을 때 예외 발생")
    void createByInvalidNumbers(final List<Integer> invalidNumbers) {
        // when, then
        assertThatThrownBy(() -> new Numbers(invalidNumbers))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining(INVALID_NUMBERS_ERR_MSG);
    }

    @Test
    void match() {
        // given
        final Numbers numbers1 = new Numbers(List.of(1, 2, 3, 4, 5, 6));
        final Numbers numbers2 = new Numbers(List.of(1, 2, 3, 7, 8, 9));

        // when
        final int matchCount = numbers1.match(numbers2);

        // then
        assertThat(matchCount).isEqualTo(3);
    }

    @ParameterizedTest
    @CsvSource(value = {"1:true", "7:false"}, delimiter = ':')
    void contains(final int target, final boolean expectedResult) {
        // given
        final Numbers numbers = new Numbers(List.of(1, 2, 3, 4, 5, 6));

        // when
        final boolean result = numbers.contains(Number.of(target));

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<List<Integer>> provideInvalidNumbers() {
        return Stream.of(
            List.of(1, 2, 3, 4, 5, 5),
            List.of(1, 2, 3, 4, 5),
            List.of(1, 2, 3, 4, 5, 6, 7)
        );
    }
}
