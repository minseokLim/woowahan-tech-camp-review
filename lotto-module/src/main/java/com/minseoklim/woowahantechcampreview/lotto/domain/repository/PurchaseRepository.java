package com.minseoklim.woowahantechcampreview.lotto.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minseoklim.woowahantechcampreview.lotto.domain.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
