package com.minseoklim.woowahantechcampreview.common.util;

import com.minseoklim.woowahantechcampreview.common.domain.Email;

public interface EmailSender {
    void send(final Email email);
}
