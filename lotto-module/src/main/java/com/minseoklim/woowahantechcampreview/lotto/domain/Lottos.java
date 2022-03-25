package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Lottos {
    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Lotto> values = new ArrayList<>();

    Lottos(final List<Lotto> lottos) {
        this.values = lottos;
    }

    Lottos withPurchase(final Purchase purchase) {
        values.forEach(it -> it.withPurchase(purchase));
        return this;
    }

    List<Lotto> getValues() {
        return Collections.unmodifiableList(values);
    }
}
