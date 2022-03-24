package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lotto {
    static final String INVALID_NUMBER_ERR_MSG = "로또의 번호들은 1~45까지의 중복되지 않는 숫자 6개로 이루어져 있어야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<Integer> numbers = new ArrayList<>();

    private LottoType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Purchase purchase;

    Lotto(final Collection<Integer> numbers, final LottoType type, final Purchase purchase) {

    }

    boolean isAuto() {
        return type == LottoType.AUTO;
    }

    public List<Integer> getNumbers() {
        return Collections.unmodifiableList(numbers);
    }
}
