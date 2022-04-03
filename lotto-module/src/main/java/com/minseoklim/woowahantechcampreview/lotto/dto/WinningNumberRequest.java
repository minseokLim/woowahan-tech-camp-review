package com.minseoklim.woowahantechcampreview.lotto.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.lotto.domain.WinningNumbers;

@Getter
public class WinningNumberRequest {
    @NotNull(message = "round는 필수값입니다.")
    private int round;

    @NotEmpty(message = "winningNumbers는 필수값입니다.")
    private List<Integer> winningNumbers;

    @NotNull(message = "bonusNumber는 필수값입니다.")
    private int bonusNumber;

    public WinningNumbers toEntity() {
        return new WinningNumbers(winningNumbers, bonusNumber);
    }
}
