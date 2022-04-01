package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.exception.BadRequestException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Purchase {
    static final String OVER_LOTTOS_ERR_MSG = "지불 금액에 비해 입력된 로또의 수가 너무 많습니다.";
    static final String PURCHASE_NOT_ALLOWED_ERR_MSG = "토요일 오후 8시 ~ 9시에는 구매가 불가능합니다.";
    private static final int BANNED_START_TIME = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Round round;

    private Long userId;

    private Lottos lottos;

    private LocalDateTime createdDate;

    public Purchase(
        final int payment,
        final Round round,
        final Long userId,
        final List<Collection<Integer>> manualNumbers
    ) {
        this(payment, round, userId, manualNumbers, LocalDateTime.now());
    }

    Purchase(
        final int payment,
        final Round round,
        final Long userId,
        final List<Collection<Integer>> manualNumbers,
        final LocalDateTime createdDate
    ) {
        this.payment = new Payment(payment);
        this.round = round;
        this.userId = userId;
        this.lottos = new Lottos(makeLottos(manualNumbers)).withPurchase(this);
        this.createdDate = createdDate;

        validate(manualNumbers, createdDate);
    }

    private void validate(final List<Collection<Integer>> manualNumbers, final LocalDateTime createdDate) {
        if (payment.calculateMaxLottoCount() < manualNumbers.size()) {
            throw new BadRequestException(OVER_LOTTOS_ERR_MSG);
        }
        if (createdDate.getDayOfWeek() == DayOfWeek.SATURDAY && createdDate.getHour() == BANNED_START_TIME) {
            throw new BadRequestException(PURCHASE_NOT_ALLOWED_ERR_MSG);
        }
    }

    private List<Lotto> makeLottos(final List<Collection<Integer>> manualNumbers) {
        final List<Lotto> result = manualNumbers.stream()
            .map(it -> new Lotto(it, Type.MANUAL))
            .collect(Collectors.toList());
        result.addAll(LottoGenerator.generate(payment.calculateMaxLottoCount() - manualNumbers.size()));

        return result;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPayment() {
        return payment.getValue();
    }

    public int getRound() {
        return round.getRound();
    }

    public List<Lotto> getLottos() {
        return lottos.getValues();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
