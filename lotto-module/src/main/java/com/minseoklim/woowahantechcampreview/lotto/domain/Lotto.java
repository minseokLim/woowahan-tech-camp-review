package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.Collection;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.lotto.config.NumbersConverter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = NumbersConverter.class)
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

    boolean isAuto() {
        return type == Type.AUTO;
    }

    public Long getId() {
        return id;
    }

    public Numbers getNumbers() {
        return numbers;
    }

    public Type getType() {
        return type;
    }
}
