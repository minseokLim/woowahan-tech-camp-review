package com.minseoklim.woowahantechcampreview.lotto.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;
import com.minseoklim.woowahantechcampreview.lotto.domain.Purchase;
import com.minseoklim.woowahantechcampreview.lotto.domain.Round;
import com.minseoklim.woowahantechcampreview.lotto.domain.repository.PurchaseRepository;
import com.minseoklim.woowahantechcampreview.lotto.domain.repository.RoundRepository;
import com.minseoklim.woowahantechcampreview.lotto.dto.PurchaseRequest;
import com.minseoklim.woowahantechcampreview.lotto.dto.PurchaseResponse;
import com.minseoklim.woowahantechcampreview.lotto.dto.WinningNumberRequest;

@Service
@Transactional
public class LottoService {
    private final PurchaseRepository purchaseRepository;
    private final RoundRepository roundRepository;

    public LottoService(final PurchaseRepository purchaseRepository, final RoundRepository roundRepository) {
        this.purchaseRepository = purchaseRepository;
        this.roundRepository = roundRepository;
    }

    public PurchaseResponse purchase(final Long userId, final PurchaseRequest purchaseRequest) {
        // TODO: 일주일에 한 번 바뀌는 값이기 때문에 캐싱이 유리
        final Round latestRound = roundRepository.findTopByOrderByIdDesc().orElseGet(this::addRound);
        final Purchase purchase = purchaseRepository.save(purchaseRequest.toEntity(latestRound, userId));
        return PurchaseResponse.of(purchase);
    }

    public Round addRound() {
        final Round lastRound = roundRepository.findTopByOrderByIdDesc().orElse(new Round(0));
        return roundRepository.saveAndFlush(new Round(lastRound.getRound() + 1));
    }

    public void applyWinningNumbers(final WinningNumberRequest winningNumberRequest) {
        final Round round = roundRepository.findByRound(winningNumberRequest.getRound())
            .orElseThrow(() -> new NotFoundException("해당 회차를 찾을 수 없습니다."));
        round.applyWinningNumbers(winningNumberRequest.toEntity());
    }
}
