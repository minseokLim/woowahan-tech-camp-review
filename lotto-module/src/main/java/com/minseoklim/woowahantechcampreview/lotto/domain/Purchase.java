package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Purchase {
    static final int LOTTO_PRICE = 1000;
    static final String PAYMENT_ERR_MSG = "지불 금액은 " + LOTTO_PRICE + "의 배수이어야 합니다.";
    static final String OVER_MANUAL_LOTTOS_ERR_MSG = "지불 금액에 비해 수동으로 입력된 로또의 수가 너무 많습니다.";
    static final String PURCHASE_NOT_ALLOWED_ERR_MSG = "토요일 오후 8시 ~ 9시에는 구매가 불가능합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal payment;

    private int round;

    private Long userId;

    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Lotto> lottos = new ArrayList<>();

    private LocalDateTime createdDate;

    Purchase(
        final int payment,
        final int round,
        final Long userId,
        final List<Collection<Integer>> manualNumbers
    ) {
        this(payment, round, userId, manualNumbers, LocalDateTime.now());
    }

    Purchase(
        final int payment,
        final int round,
        final Long userId,
        final List<Collection<Integer>> manualNumbers,
        final LocalDateTime createdDate
    ) {

    }

    public List<Lotto> getLottos() {
        return Collections.unmodifiableList(lottos);
    }
}
