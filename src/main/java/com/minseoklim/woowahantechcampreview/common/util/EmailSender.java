package com.minseoklim.woowahantechcampreview.common.util;

import com.minseoklim.woowahantechcampreview.common.dto.EmailDto;

public interface EmailSender {
    void send(final EmailDto emailDto);
}
