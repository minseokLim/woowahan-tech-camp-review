package com.minseoklim.woowahantechcampreview.lotto.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minseoklim.woowahantechcampreview.lotto.domain.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {
    Optional<Round> findTopByOrderByIdDesc();
}
