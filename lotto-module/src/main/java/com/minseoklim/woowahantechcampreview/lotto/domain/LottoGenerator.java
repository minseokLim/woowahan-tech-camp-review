package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface LottoGenerator {
    static List<Lotto> generate(final int count) {
        final List<Lotto> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(generateLotto());
        }
        return result;
    }

    private static Lotto generateLotto() {
        final List<Integer> numbers = new ArrayList<>();
        for (int i = Numbers.MIN; i <= Numbers.MAX; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return new Lotto(numbers.subList(0, Numbers.SIZE), Type.AUTO);
    }
}
