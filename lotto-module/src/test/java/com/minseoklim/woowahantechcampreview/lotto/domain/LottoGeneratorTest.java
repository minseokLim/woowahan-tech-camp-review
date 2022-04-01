package com.minseoklim.woowahantechcampreview.lotto.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LottoGeneratorTest {
    @Test
    void generate() {
        assertThat(LottoGenerator.generate(3)).hasSize(3);
    }
}
