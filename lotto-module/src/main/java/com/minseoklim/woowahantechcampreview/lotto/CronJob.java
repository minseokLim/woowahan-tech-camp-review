package com.minseoklim.woowahantechcampreview.lotto;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.minseoklim.woowahantechcampreview.lotto.application.LottoService;

// TODO 확장성을 생각하여 별도의 서버에 스프링 배치 등을 이용하여 구현하는 것이 옳지만 토이 프로젝트라 간단히 구현
@EnableScheduling
@Component
public class CronJob {
    private final LottoService lottoService;

    public CronJob(final LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @Scheduled(cron = "0 50 20 ? * SAT")
    public void addRound() {
        lottoService.addRound();
    }
}
