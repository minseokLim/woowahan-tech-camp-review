package com.minseoklim.woowahantechcampreview.lotto.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    private int round;

    private Long userId;

    private Lottos lottos;

    private LocalDateTime createdDate;

    Purchase(final int payment, final int round, final Long userId, final List<Lotto> lottos) {
        this(payment, round, userId, lottos, LocalDateTime.now());
    }

    Purchase(
        final int payment,
        final int round,
        final Long userId,
        final List<Lotto> lottos,
        final LocalDateTime createdDate
    ) {
        this.payment = new Payment(payment);
        this.round = round;
        this.userId = userId;
        this.lottos = new Lottos(lottos).withPurchase(this);
        this.createdDate = createdDate;

        validate(lottos);
    }

    private void validate(final List<Lotto> lottos) {
        if (payment.calculateMaxLottoCount() < lottos.size()) {
            throw new BadRequestException(OVER_LOTTOS_ERR_MSG);
        }
        if (createdDate.getDayOfWeek() == DayOfWeek.SATURDAY && createdDate.getHour() == BANNED_START_TIME) {
            throw new BadRequestException(PURCHASE_NOT_ALLOWED_ERR_MSG);
        }
    }

    List<Lotto> getLottos() {
        return lottos.getValues();
    }
}
