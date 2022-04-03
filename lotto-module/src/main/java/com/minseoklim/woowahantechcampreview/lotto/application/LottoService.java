package com.minseoklim.woowahantechcampreview.lotto.application;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;
import com.minseoklim.woowahantechcampreview.lotto.domain.Purchase;
import com.minseoklim.woowahantechcampreview.lotto.domain.Round;
import com.minseoklim.woowahantechcampreview.lotto.domain.repository.PurchaseRepository;
import com.minseoklim.woowahantechcampreview.lotto.domain.repository.PurchaseRepositorySupport;
import com.minseoklim.woowahantechcampreview.lotto.domain.repository.RoundRepository;
import com.minseoklim.woowahantechcampreview.lotto.dto.PurchaseRequest;
import com.minseoklim.woowahantechcampreview.lotto.dto.PurchaseResponse;
import com.minseoklim.woowahantechcampreview.lotto.dto.WinningNumberRequest;

@Service
@Transactional
public class LottoService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseRepositorySupport purchaseRepositorySupport;
    private final RoundRepository roundRepository;

    public LottoService(
        final PurchaseRepository purchaseRepository,
        final PurchaseRepositorySupport purchaseRepositorySupport,
        final RoundRepository roundRepository
    ) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseRepositorySupport = purchaseRepositorySupport;
        this.roundRepository = roundRepository;
    }

    public PurchaseResponse purchase(final Long userId, final PurchaseRequest purchaseRequest) {
        // TODO: 일주일에 한 번 바뀌는 값이기 때문에 캐싱이 유리
        final Round latestRound = roundRepository.findTopByOrderByIdDesc().orElseGet(this::addRound);
        final Purchase purchase = purchaseRepository.save(purchaseRequest.toEntity(latestRound, userId));
        return PurchaseResponse.of(purchase);
    }

    public Round addRound() {
        final LocalDateTime winningNumberOpeningTime = Round.computeOpeningTime(LocalDateTime.now());
        final int lastRound = roundRepository.findTopByOrderByIdDesc().map(Round::getRound).orElse(0);
        return roundRepository.saveAndFlush(new Round(lastRound + 1, winningNumberOpeningTime));
    }

    public void applyWinningNumbers(final WinningNumberRequest winningNumberRequest) {
        final Round round = roundRepository.findByRound(winningNumberRequest.getRound())
            .orElseThrow(() -> new NotFoundException("해당 회차를 찾을 수 없습니다."));
        round.applyWinningNumbers(winningNumberRequest.toEntity());
    }

    public Page<PurchaseResponse> getMyLottoPurchases(final Long userId, final Pageable pageable) {
        final Page<Purchase> purchases = purchaseRepositorySupport.findAllByUserId(userId, pageable);
        return purchases.map(PurchaseResponse::of);
    }
}
