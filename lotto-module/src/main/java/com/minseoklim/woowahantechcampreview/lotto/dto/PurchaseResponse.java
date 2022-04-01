package com.minseoklim.woowahantechcampreview.lotto.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Purchase;

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
        return new PurchaseResponse(
            purchase.getId(),
            purchase.getPayment().intValue(),
            purchase.getRound(),
            purchase.getLottos().stream()
                .map(LottoResponse::of)
                .collect(Collectors.toUnmodifiableList()),
            purchase.getCreatedDate()
        );
    }
}
