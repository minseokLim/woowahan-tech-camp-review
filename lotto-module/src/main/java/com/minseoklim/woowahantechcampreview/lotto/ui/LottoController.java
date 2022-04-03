package com.minseoklim.woowahantechcampreview.lotto.ui;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minseoklim.woowahantechcampreview.auth.config.annotation.AuthenticatedUsername;
import com.minseoklim.woowahantechcampreview.auth.config.annotation.CheckAdminPermission;
import com.minseoklim.woowahantechcampreview.lotto.application.LottoService;
import com.minseoklim.woowahantechcampreview.lotto.dto.PurchaseRequest;
import com.minseoklim.woowahantechcampreview.lotto.dto.PurchaseResponse;
import com.minseoklim.woowahantechcampreview.lotto.dto.WinningNumberRequest;

@RestController
@RequestMapping("/lottos")
public class LottoController {
    private final LottoService lottoService;

    public LottoController(final LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> purchase(
        @AuthenticatedUsername final Long userId,
        @Valid @RequestBody final PurchaseRequest purchaseRequest
    ) {
        final PurchaseResponse purchase = lottoService.purchase(userId, purchaseRequest);
        final URI uri = URI.create("/lottos" + purchase.getId());
        return ResponseEntity.created(uri).body(purchase);
    }

    @PostMapping("/rounds")
    @CheckAdminPermission
    public ResponseEntity<Void> addRound() {
        lottoService.addRound();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/rounds")
    @CheckAdminPermission
    public ResponseEntity<Void> applyWinningNumbers(
        @Valid @RequestBody final WinningNumberRequest winningNumberRequest
    ) {
        lottoService.applyWinningNumbers(winningNumberRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
