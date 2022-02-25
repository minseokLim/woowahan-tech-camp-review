package com.minseoklim.woowahantechcampreview.user.domain;

import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, String> {
}
