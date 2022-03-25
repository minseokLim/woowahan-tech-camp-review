package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.Collection;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Numbers numbers;

    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Purchase purchase;

    Lotto(final Collection<Integer> numbers, final Type type) {
        this.numbers = new Numbers(numbers);
        this.type = type;
    }

    void withPurchase(final Purchase purchase) {
        if (this.purchase != null) {
            throw new IllegalArgumentException("구입 내역이 이미 설정되었습니다.");
        }
        this.purchase = purchase;
    }

    List<Integer> getNumbers() {
        return numbers.getValues();
    }
}
