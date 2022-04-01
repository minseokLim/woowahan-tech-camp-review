package com.minseoklim.woowahantechcampreview.lotto.dto;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.lotto.domain.Purchase;
import com.minseoklim.woowahantechcampreview.lotto.domain.Round;

@Getter
public class PurchaseRequest {
    @NotNull(message = "payment는 필수값입니다.")
    private int payment;
    private List<Collection<Integer>> manualNumbers;

    public Purchase toEntity(final Round round, final Long userId) {
        return new Purchase(payment, round, userId, manualNumbers);
    }
}
