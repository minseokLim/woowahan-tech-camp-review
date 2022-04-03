package com.minseoklim.woowahantechcampreview.lotto.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Purchase;
import com.minseoklim.woowahantechcampreview.lotto.domain.Round;


@Getter
public class PurchaseResponse {
    private final Long id;

    private final int payment;

    private final int round;

    private final List<LottoResponse> lottos;

    private final LocalDateTime createdDate;

    public PurchaseResponse(
        final Long id,
        final int payment,
        final int round,
        final List<LottoResponse> lottos,
        final LocalDateTime createdDate
    ) {
        this.id = id;
        this.payment = payment;
        this.round = round;
        this.lottos = lottos;
        this.createdDate = createdDate;
    }

    public static PurchaseResponse of(final Purchase purchase) {
        final Round round = purchase.getRound();
        final LocalDateTime now = LocalDateTime.now();

        return new PurchaseResponse(
            purchase.getId(),
            purchase.getPayment().intValue(),
            round.getRound(),
            purchase.getLottos().stream()
                .map(it -> LottoResponse.of(it, round.computeRank(it, now)))
                .collect(Collectors.toUnmodifiableList()),
            purchase.getCreatedDate()
        );
    }
}
