package com.minseoklim.woowahantechcampreview.common.util;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.minseoklim.woowahantechcampreview.common.dto.EmailDto;

@Profile("test")
@Component
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(final EmailDto emailDto) {
        // do nothing
    }
}
